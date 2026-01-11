package ru.yandex.practicum.sht.commerce.contract;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.ApiError;
import ru.yandex.practicum.sht.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public interface ShoppingCartContract {

    @GetMapping
    ShoppingCartDto getAuthorizedUserCart(@RequestParam String username) throws NotAuthorizedUserException;

    @PutMapping
    ShoppingCartDto putProductToCart(@RequestParam String username,
                                     @RequestBody Map<UUID, Long> productByQuantity) throws NotAuthorizedUserException;

    @DeleteMapping
    void deleteUserCartActivation(@RequestParam String username) throws NotAuthorizedUserException;

    @PostMapping("/remove")
    ShoppingCartDto postDeleteProductFromUserCart(@RequestParam String username, @RequestBody Set<UUID> productList)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException;

    @PostMapping("/change-quantity")
    ShoppingCartDto postChangeProductQuantityInUserCart(@RequestParam String username,
                                                        @RequestBody ChangeProductQuantityRequest request)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException;

    @ExceptionHandler(NotAuthorizedUserException.class)
    default ApiError onNotAuthorizedUser(NotAuthorizedUserException exception) {
        return ApiError.from(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    default ApiError onNoProductInShoppingCart(NoProductsInShoppingCartException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    default ApiError onConstraintViolation(ConstraintViolationException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }
}