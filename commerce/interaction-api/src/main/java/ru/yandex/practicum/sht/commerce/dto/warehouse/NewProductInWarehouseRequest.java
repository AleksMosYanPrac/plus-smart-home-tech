package ru.yandex.practicum.sht.commerce.dto.warehouse;

import lombok.Data;

import java.util.UUID;

@Data
public class NewProductInWarehouseRequest {
    private UUID productId;
    private Boolean fragile;
    private DimensionDto dimension;
    private Double weight;
}
