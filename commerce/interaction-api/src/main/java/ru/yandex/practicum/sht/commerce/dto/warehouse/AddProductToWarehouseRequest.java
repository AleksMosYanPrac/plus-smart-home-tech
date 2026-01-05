package ru.yandex.practicum.sht.commerce.dto.warehouse;

import lombok.Data;

import java.util.UUID;

@Data
public class AddProductToWarehouseRequest {
    private UUID productId;
    private Long quantity;
}