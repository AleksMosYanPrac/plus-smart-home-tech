package ru.yandex.practicum.sht.commerce.dto.cart;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangeProductQuantityRequest {
    private UUID productId;
    private Long newQuantity;
}