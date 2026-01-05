package ru.yandex.practicum.sht.commerce.contract;

import jakarta.validation.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.dto.store.ProductCategory;
import ru.yandex.practicum.sht.commerce.dto.store.ProductDto;
import ru.yandex.practicum.sht.commerce.dto.store.QuantityState;
import ru.yandex.practicum.sht.commerce.dto.ApiError;
import ru.yandex.practicum.sht.commerce.exception.ProductNotFoundException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

public interface ShoppingStoreContract {

    @GetMapping
    Page<ProductDto> getProductList(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    ProductDto putNewProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto) throws ProductNotFoundException;

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody UUID productId) throws ProductNotFoundException;

    @PostMapping("/quantityState")
    boolean updateProductQuantityState(@RequestParam UUID productId,
                                       @RequestParam QuantityState quantityState) throws ProductNotFoundException;

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId) throws ProductNotFoundException;

    @ExceptionHandler(ProductNotFoundException.class)
    default ApiError onNotAuthorizedUser(ProductNotFoundException exception) {
        return ApiError.from(exception, NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    default ApiError onConstraintViolation(ConstraintViolationException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }
}