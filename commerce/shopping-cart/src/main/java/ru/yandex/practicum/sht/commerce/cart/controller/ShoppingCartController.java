package ru.yandex.practicum.sht.commerce.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.sht.commerce.cart.service.ShoppingCartService;
import ru.yandex.practicum.sht.commerce.contract.ShoppingCartContract;
import ru.yandex.practicum.sht.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartContract {

    private final ShoppingCartService cartService;

    @Override
    public ShoppingCartDto getAuthorizedUserCart(String username) throws NotAuthorizedUserException {
        return cartService.getUserShoppingCart(username);
    }

    @Override
    public ShoppingCartDto putProductToCart(String username, Map<UUID, Long> productByQuantity)
            throws NotAuthorizedUserException {
        return cartService.addProductToCart(username, productByQuantity);
    }

    @Override
    public void deleteUserCartActivation(String username) throws NotAuthorizedUserException {
        cartService.deactivateCart(username);
    }

    @Override
    public ShoppingCartDto postDeleteProductFromUserCart(String username,
                                                         Set<UUID> productList)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException {
        return cartService.deleteProductFromCart(username, productList);
    }

    @Override
    public ShoppingCartDto postChangeProductQuantityInUserCart(String username,
                                                               ChangeProductQuantityRequest request)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException {
        return cartService.changeProductQuantityInCart(username, request);
    }
}