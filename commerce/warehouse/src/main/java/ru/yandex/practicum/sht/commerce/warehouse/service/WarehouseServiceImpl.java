package ru.yandex.practicum.sht.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.*;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.sht.commerce.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.sht.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.sht.commerce.warehouse.model.OrderBooking;
import ru.yandex.practicum.sht.commerce.warehouse.model.Product;
import ru.yandex.practicum.sht.commerce.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.sht.commerce.warehouse.repository.WarehouseRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.sht.commerce.warehouse.config.AddressConfig.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final OrderBookingRepository bookingRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper mapper;
    private final Address address;
    private final TransactionTemplate transactionTemplate;

    @Override
    @Transactional
    public void addNewProduct(NewProductInWarehouseRequest request) throws SpecifiedProductAlreadyInWarehouseException {
        log.info("Add new product to warehouse with ID:{} ", request.getProductId());
        if (warehouseRepository.existsById((request.getProductId()))) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already exists at warehouse");
        }
        warehouseRepository.save(mapper.toProduct(request));
    }

    @Override
    public BookedProductsDto checkProductQuantity(ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse {
        log.info("Check product quantity in warehouse for cart ID:{} ", cart.getShoppingCartId());
        Map<UUID, Product> products = warehouseRepository.findAllById(cart.getProducts().keySet())
                .stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        Map<UUID, Long> productsInCart = cart.getProducts();
        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        for (UUID productId : productsInCart.keySet()) {
            if (Objects.nonNull(products.get(productId)) &&
                products.get(productId).getQuantity().compareTo(productsInCart.get(productId)) >= 0) {
                calculateBookedProducts(bookedProductsDto, products.get(productId), productsInCart.get(productId));
            } else {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Quantity at warehouse low than shopping cart");
            }
        }
        return bookedProductsDto;
    }

    @Override
    @Transactional
    public void increaseProductQuantity(AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException {
        log.info("Add quantity for Product ID:{}", request.getProductId());
        Product product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Product is absent at warehouse"));
        product.addQuantity(request.getQuantity());
        warehouseRepository.save(product);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return mapper.toAddressDto(address);
    }

    @Override
    @Transactional
    public void transferProductsToDelivery(ShippedToDeliveryRequest request) {
        log.info("Transfer products to delivery for order:{}", request.getOrderId());
        OrderBooking booking = bookingRepository.findByOrderId(request.getOrderId());
        booking.setDeliveryId(request.getDeliveryId());
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void returnProductsToWarehouse(Map<UUID, Long> products) {
        log.info("Returning products to warehouse");
        List<Product> productList = warehouseRepository.findAllById(products.keySet());
        productList.forEach(p -> {
            p.addQuantity(products.get(p.getProductId()));
        });
        warehouseRepository.saveAll(productList);
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProductsForOrderFromShoppingCart(AssemblyProductsForOrderRequest request)
            throws ProductInShoppingCartLowQuantityInWarehouse {
        List<Product> products = warehouseRepository.findAllById(request.getProducts().keySet());
        products.forEach(product -> {
            if (product.getQuantity() < request.getProducts().get(product.getProductId())) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Quantity at warehouse low then in request");
            }
        });
        OrderBooking booking = new OrderBooking();
        booking.setOrderId(request.getOrderId());
        booking.setProducts(request.getProducts());
        bookingRepository.save(booking);

        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        products.forEach(product -> {
            Long productQuantityAtRequest = request.getProducts().get(product.getProductId());
            product.reduceQuantity(productQuantityAtRequest);
            calculateBookedProducts(bookedProductsDto, product, productQuantityAtRequest);
        });
        warehouseRepository.saveAll(products);
        return bookedProductsDto;
    }

    private void calculateBookedProducts(BookedProductsDto products, Product product, Long quantity) {
        double weight = product.getWeight() * quantity;
        double volume = product.getVolume() * quantity;
        products.setDeliveryWeight(products.getDeliveryWeight() + weight);
        products.setDeliveryVolume(products.getDeliveryVolume() + volume);
        if (product.getFragile()) {
            products.setFragile(true);
        }
    }
}