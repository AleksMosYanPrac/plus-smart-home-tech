package ru.yandex.practicum.sht.commerce.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.sht.commerce.contract.ShoppingStoreContract;
import ru.yandex.practicum.sht.commerce.dto.store.ProductCategory;
import ru.yandex.practicum.sht.commerce.dto.store.ProductDto;
import ru.yandex.practicum.sht.commerce.dto.store.QuantityState;
import ru.yandex.practicum.sht.commerce.exception.ProductNotFoundException;
import ru.yandex.practicum.sht.commerce.store.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreContract {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    public Page<ProductDto> getProductList(ProductCategory category, Pageable pageable) {
        return shoppingStoreService.getProductList(category,pageable);
    }

    @Override
    public ProductDto putNewProduct(ProductDto productDto) {
        return shoppingStoreService.addNewProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) throws ProductNotFoundException {
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    public boolean removeProduct(UUID productId) throws ProductNotFoundException {
        return shoppingStoreService.removeProduct(productId);
    }

    @Override
    public boolean updateProductQuantityState(UUID productId, QuantityState quantityState) throws ProductNotFoundException {
        return shoppingStoreService.updateProductQuantityState(productId,quantityState);
    }

    @Override
    public ProductDto getProductById(UUID productId) throws ProductNotFoundException {
        return shoppingStoreService.getProductById(productId);
    }
}