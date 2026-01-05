package ru.yandex.practicum.sht.commerce.contract;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.dto.ApiError;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.sht.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.exception.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public interface WarehouseContract {

    @PutMapping
    void putNewProduct(@RequestBody NewProductInWarehouseRequest request) throws
            SpecifiedProductAlreadyInWarehouseException;

    @PostMapping("/check")
    BookedProductsDto postCheckProductQuantityForShoppingCart(@RequestBody ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse;

    @PostMapping("/add")
    void postAddProductToWarehouse(@RequestBody AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException;

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    default ApiError onProductAlreadyExists(SpecifiedProductAlreadyInWarehouseException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    default ApiError onNotEnoughProductQuantity(ProductInShoppingCartLowQuantityInWarehouse exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    default ApiError onNotAuthorizedUser(NoSpecifiedProductInWarehouseException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    default ApiError onConstraintViolation(ConstraintViolationException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }
}