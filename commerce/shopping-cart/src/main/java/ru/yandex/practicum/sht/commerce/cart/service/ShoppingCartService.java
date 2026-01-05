package ru.yandex.practicum.sht.commerce.cart.service;

import ru.yandex.practicum.sht.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getUserShoppingCart(String userName) throws NotAuthorizedUserException;

    ShoppingCartDto addProductToCart(String userName, Map<UUID, Long> productByQuantity) throws NotAuthorizedUserException;

    void deactivateCart(String userName) throws NotAuthorizedUserException;

    ShoppingCartDto deleteProductFromCart(String userName, Set<UUID> productList)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException;

    ShoppingCartDto changeProductQuantityInCart(String userName, ChangeProductQuantityRequest request)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException;
}