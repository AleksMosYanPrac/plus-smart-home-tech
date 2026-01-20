package ru.yandex.practicum.sht.commerce.order.service;

import feign.Retryer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.sht.commerce.contract.DeliveryFeignClient;
import ru.yandex.practicum.sht.commerce.contract.PaymentFeignClient;
import ru.yandex.practicum.sht.commerce.contract.ShoppingCartFeignClient;
import ru.yandex.practicum.sht.commerce.contract.WarehouseFeignClient;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.order.OrderState;
import ru.yandex.practicum.sht.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.sht.commerce.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.sht.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;
import ru.yandex.practicum.sht.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.sht.commerce.order.model.Order;
import ru.yandex.practicum.sht.commerce.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static ru.yandex.practicum.sht.commerce.dto.order.OrderState.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final DeliveryFeignClient deliveryClient;
    private final PaymentFeignClient paymentClient;
    private final WarehouseFeignClient warehouseClient;
    private final ShoppingCartFeignClient shoppingCartClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final TransactionTemplate transactionTemplate;

    private final Retryer retryer;

    @Override
    public List<OrderDto> getUserOrders(String username) throws NotAuthorizedUserException {
        log.info("Get all orders by username:{}", username);
        ShoppingCartDto cart = shoppingCartClient.getAuthorizedUserCart(username);
        return orderRepository.findAllByShoppingCartId(cart.getShoppingCartId()).stream().map(mapper::toDto).toList();
    }

    @Override
    public OrderDto addNewOrder(CreateNewOrderRequest orderRequest) throws NoSpecifiedProductInWarehouseException {
        log.info("Create new order for shopping cart:{}", orderRequest.getShoppingCart().getShoppingCartId());
        Order newOrder = transactionTemplate.execute(status -> {
            Order order = new Order();
            order.setShoppingCartId(orderRequest.getShoppingCart().getShoppingCartId());
            order.setState(NEW);
            return orderRepository.save(order);
        });

        log.debug("Send request to warehouse for booking products Order:{}", newOrder.getOrderId());
        AssemblyProductsForOrderRequest warehousRequest = new AssemblyProductsForOrderRequest();
        warehousRequest.setOrderId(newOrder.getOrderId());
        warehousRequest.setProducts(orderRequest.getShoppingCart().getProducts());
        BookedProductsDto bookedProducts = warehouseClient.postAssemblyProductForOrderFromShoppingCart(warehousRequest);
        Order orderWithBookedProducts = transactionTemplate.execute(status -> {
            newOrder.setProducts(orderRequest.getShoppingCart().getProducts());
            newOrder.setDeliveryWeight(bookedProducts.getDeliveryWeight());
            newOrder.setDeliveryVolume(bookedProducts.getDeliveryVolume());
            newOrder.setFragile(bookedProducts.isFragile());
            return orderRepository.save(newOrder);
        });

        log.debug("Send request to delivery for create new delivery Order:{}", newOrder.getOrderId());
        DeliveryDto deliveryRequest = new DeliveryDto();
        deliveryRequest.setOrderId(newOrder.getOrderId());
        deliveryRequest.setFromAddress(warehouseClient.getWarehouseAddress());
        deliveryRequest.setToAddress(orderRequest.getDeliveryAddress());
        DeliveryDto deliveryDto = deliveryClient.putAddNewDelivery(deliveryRequest);
        return transactionTemplate.execute(status -> {
            orderWithBookedProducts.setDeliveryId(deliveryDto.getDeliveryId());
            return mapper.toDto(orderRepository.save(orderWithBookedProducts));
        });
    }

    @Override
    public OrderDto addReturnOrder(ProductReturnRequest request) throws NoOrderFoundException {
        log.info("Returning products for order:{}", request.getOrderId());
        Order orderWithReturnedProducts = transactionTemplate.execute(status -> {
            Order order = getById(request.getOrderId());
            if (order.getProducts().keySet().containsAll(request.getProducts().keySet())) {
                order.setState(CANCELED);
                return orderRepository.save(order);
            } else {
                order.setState(PRODUCT_RETURNED);
                return orderRepository.save(order);
            }
        });
        warehouseClient.postReturnProductToWarehouse(request.getProducts());
        return mapper.toDto(orderWithReturnedProducts);
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) throws NoOrderFoundException {
        log.info("Calculate total price for order:{}", orderId);
        Order order = getById(orderId);
        BigDecimal totalPrice = paymentClient.postCalcTotalCost(mapper.toDto(order));
        BigDecimal productPrice = paymentClient.postCalcProductsCostInOrder(mapper.toDto(order));
        return transactionTemplate.execute(status -> {
            order.setTotalPrice(totalPrice);
            order.setProductPrice(productPrice);
            order.setState(ON_PAYMENT);
            return mapper.toDto(orderRepository.save(order));
        });
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) throws NoOrderFoundException {
        log.info("Calculate delivery price for order:{}", orderId);
        Order order = getById(orderId);
        BigDecimal deliveryPrice = deliveryClient.postCalcDeliveryCost(mapper.toDto(order));
        return transactionTemplate.execute(status -> {
            order.setDeliveryPrice(deliveryPrice);
            order.setState(ON_DELIVERY);
            return mapper.toDto(orderRepository.save(order));
        });
    }

    @Override
    @Transactional
    public OrderDto changeOrderState(UUID orderId, OrderState state) throws NoOrderFoundException {
        log.info("Change state for order:{}", orderId);
        Order order = getById(orderId);
        order.setState(state);
        return mapper.toDto(orderRepository.save(order));
    }

    private Order getById(UUID orderId) throws NoOrderFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID:" + orderId));
    }
}