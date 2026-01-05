package ru.yandex.practicum.sht.commerce.dto.warehouse;

import lombok.Data;

@Data
public class BookedProductsDto {
    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;
}