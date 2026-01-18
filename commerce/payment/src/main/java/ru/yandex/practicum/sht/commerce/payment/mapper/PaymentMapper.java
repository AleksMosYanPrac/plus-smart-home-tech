package ru.yandex.practicum.sht.commerce.payment.mapper;

import ru.yandex.practicum.sht.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.payment.model.Payment;

public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}