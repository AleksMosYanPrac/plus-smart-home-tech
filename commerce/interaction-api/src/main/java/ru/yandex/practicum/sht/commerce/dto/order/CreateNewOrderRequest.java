package ru.yandex.practicum.sht.commerce.dto.order;

import lombok.Data;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;

@Data
public class CreateNewOrderRequest {
    private ShoppingCartDto shoppingCart;
    private AddressDto deliveryAddress;
}