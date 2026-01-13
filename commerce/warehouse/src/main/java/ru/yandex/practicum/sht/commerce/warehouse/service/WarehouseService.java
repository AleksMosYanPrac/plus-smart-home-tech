package ru.yandex.practicum.sht.commerce.warehouse.service;

import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.*;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.sht.commerce.exception.SpecifiedProductAlreadyInWarehouseException;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest request) throws SpecifiedProductAlreadyInWarehouseException;

    BookedProductsDto checkProductQuantity(ShoppingCartDto cart) throws ProductInShoppingCartLowQuantityInWarehouse;

    void increaseProductQuantity(AddProductToWarehouseRequest request) throws NoSpecifiedProductInWarehouseException;

    AddressDto getWarehouseAddress();

    void transferProductsToDelivery(ShippedToDeliveryRequest request);

    void returnProductsToWarehouse(Map<UUID, Long> products);

    BookedProductsDto assemblyProductsForOrderFromShoppingCart(AssemblyProductsForOrderRequest request)
            throws ProductInShoppingCartLowQuantityInWarehouse;
}