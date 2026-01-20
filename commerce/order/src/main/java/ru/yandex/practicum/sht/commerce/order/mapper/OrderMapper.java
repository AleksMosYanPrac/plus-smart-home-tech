package ru.yandex.practicum.sht.commerce.order.mapper;

import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.order.model.Order;

public interface OrderMapper {
    OrderDto toDto(Order order);
}