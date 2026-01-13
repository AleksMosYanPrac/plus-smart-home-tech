package ru.yandex.practicum.sht.commerce.cart.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.sht.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;

@Component
public class ShoppingCartMapperImpl implements ShoppingCartMapper {
    @Override
    public ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setShoppingCartId(shoppingCart.getShoppingCartId());
        shoppingCartDto.setProducts(shoppingCart.getProducts());
        return shoppingCartDto;
    }
}