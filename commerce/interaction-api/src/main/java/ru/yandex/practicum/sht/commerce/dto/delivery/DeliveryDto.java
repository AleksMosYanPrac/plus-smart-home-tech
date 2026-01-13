package ru.yandex.practicum.sht.commerce.dto.delivery;

import lombok.Data;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;

import java.util.UUID;

@Data
public class DeliveryDto {
    private UUID deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private UUID orderId;
    private DeliveryState deliveryState;
}