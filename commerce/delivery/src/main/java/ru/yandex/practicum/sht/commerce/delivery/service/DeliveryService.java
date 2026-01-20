package ru.yandex.practicum.sht.commerce.delivery.service;

import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryState;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto addNewDelivery(DeliveryDto deliveryDto);

    void changeDeliveryState(UUID deliveryId, DeliveryState deliveryState) throws NoDeliveryFoundException;

    BigDecimal calculateDeliveryPrice(OrderDto orderDto) throws NoDeliveryFoundException;
}