package ru.yandex.practicum.sht.commerce.cart.mapper;

import ru.yandex.practicum.sht.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;

public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}