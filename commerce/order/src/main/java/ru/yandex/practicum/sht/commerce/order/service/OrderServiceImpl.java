package ru.yandex.practicum.sht.commerce.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Override
    public List<OrderDto> getUserOrders(String username) throws NotAuthorizedUserException {
        log.info("Get all orders by username:{}", username);
        ShoppingCartDto cart = shoppingCartClient.getAuthorizedUserCart(username);
        return orderRepository.findAllByShoppingCartId(cart.getShoppingCartId()).stream().map(mapper::toDto).toList();
    }

    @Override
    public OrderDto addNewOrder(CreateNewOrderRequest orderRequest) throws NoSpecifiedProductInWarehouseException {
        log.info("Create new order for shopping cart:{}", orderRequest.getShoppingCart().getShoppingCartId());
        Order newOrder = new Order();
        newOrder.setShoppingCartId(orderRequest.getShoppingCart().getShoppingCartId());
        newOrder.setState(NEW);
        newOrder = orderRepository.save(newOrder);

        log.debug("Send request to warehouse for booking products Order:{}", newOrder.getOrderId());
        AssemblyProductsForOrderRequest warehousRequest = new AssemblyProductsForOrderRequest();
        warehousRequest.setOrderId(newOrder.getOrderId());
        warehousRequest.setProducts(orderRequest.getShoppingCart().getProducts());
        BookedProductsDto bookedProducts = warehouseClient.postAssemblyProductForOrderFromShoppingCart(warehousRequest);
        newOrder.setProducts(orderRequest.getShoppingCart().getProducts());
        newOrder.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        newOrder.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        newOrder.setFragile(bookedProducts.isFragile());
        orderRepository.save(newOrder);

        log.debug("Send request to delivery for create new delivery Order:{}", newOrder.getOrderId());
        DeliveryDto deliveryRequest = new DeliveryDto();
        deliveryRequest.setOrderId(newOrder.getOrderId());
        deliveryRequest.setFromAddress(warehouseClient.getWarehouseAddress());
        deliveryRequest.setToAddress(orderRequest.getDeliveryAddress());
        DeliveryDto deliveryDto = deliveryClient.putAddNewDelivery(deliveryRequest);
        newOrder.setDeliveryId(deliveryDto.getDeliveryId());

        return mapper.toDto(orderRepository.save(newOrder));
    }

    @Override
    public OrderDto addReturnOrder(ProductReturnRequest request) throws NoOrderFoundException {
        log.info("Returning products for order:{}", request.getOrderId());
        Order order = getById(request.getOrderId());
        if (order.getProducts().keySet().containsAll(request.getProducts().keySet())) {
            warehouseClient.postReturnProductToWarehouse(request.getProducts());
            order.setState(CANCELED);
            return mapper.toDto(orderRepository.save(order));
        } else {
            warehouseClient.postReturnProductToWarehouse(request.getProducts());
            order.setState(PRODUCT_RETURNED);
            return mapper.toDto(orderRepository.save(order));
        }
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) throws NoOrderFoundException {
        log.info("Calculate total price for order:{}", orderId);
        Order order = getById(orderId);
        order.setTotalPrice(paymentClient.postCalcTotalCost(mapper.toDto(order)));
        order.setProductPrice(paymentClient.postCalcProductsCostInOrder(mapper.toDto(order)));
        order.setState(ON_PAYMENT);
        return mapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) throws NoOrderFoundException {
        log.info("Calculate delivery price for order:{}", orderId);
        Order order = getById(orderId);
        order.setDeliveryPrice(deliveryClient.postCalcDeliveryCost(mapper.toDto(order)));
        order.setState(ON_DELIVERY);
        return mapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto changeOrderState(UUID orderId, OrderState state) throws NoOrderFoundException {
        Order order = getById(orderId);
        order.setState(state);
        return mapper.toDto(orderRepository.save(order));
    }

    private Order getById(UUID orderId) throws NoOrderFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID:" + orderId));
    }
}