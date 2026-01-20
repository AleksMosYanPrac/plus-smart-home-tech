package ru.yandex.practicum.sht.commerce.payment.service;

import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NotEnoughInfoInOrderToCalculateException;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto addPayment(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException;

    BigDecimal calculateTotalCost(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException;

    BigDecimal calculateProductsCost(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException;

    void receivePayment(UUID paymentId, boolean paymentResult) throws NoOrderFoundException;
}