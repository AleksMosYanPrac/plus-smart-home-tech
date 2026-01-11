package ru.yandex.practicum.sht.commerce.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.sht.commerce.dto.store.ProductCategory;
import ru.yandex.practicum.sht.commerce.dto.store.ProductDto;
import ru.yandex.practicum.sht.commerce.dto.store.QuantityState;
import ru.yandex.practicum.sht.commerce.exception.ProductNotFoundException;

import java.util.UUID;

public interface ShoppingStoreService {
    Page<ProductDto> getProductList(ProductCategory category, Pageable pageable);

    ProductDto addNewProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto) throws ProductNotFoundException;

    boolean removeProduct(UUID productId) throws ProductNotFoundException;

    boolean updateProductQuantityState(UUID productId, QuantityState quantityState) throws ProductNotFoundException;

    ProductDto getProductById(UUID productId) throws ProductNotFoundException;
}