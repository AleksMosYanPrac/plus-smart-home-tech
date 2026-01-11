package ru.yandex.practicum.sht.commerce.warehouse.service;

import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.sht.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.sht.commerce.exception.SpecifiedProductAlreadyInWarehouseException;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest request) throws SpecifiedProductAlreadyInWarehouseException;

    BookedProductsDto checkProductQuantity(ShoppingCartDto cart) throws ProductInShoppingCartLowQuantityInWarehouse;

    void increaseProductQuantity(AddProductToWarehouseRequest request) throws NoSpecifiedProductInWarehouseException;

    AddressDto getWarehouseAddress();
}