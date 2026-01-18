package ru.yandex.practicum.sht.commerce.delivery.mapper;

import ru.yandex.practicum.sht.commerce.delivery.model.Delivery;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;

public interface DeliveryMapper {
    Delivery.Address toAddress(AddressDto addressDto);

    DeliveryDto toDto(Delivery delivery);
}