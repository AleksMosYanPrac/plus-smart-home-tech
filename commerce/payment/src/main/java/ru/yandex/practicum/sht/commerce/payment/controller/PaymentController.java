package ru.yandex.practicum.sht.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.sht.commerce.contract.PaymentContract;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.sht.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentContract {

    private final PaymentService paymentService;

    @Override
    public PaymentDto postPaymentRequest(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException {
        return paymentService.addPayment(orderDto);
    }

    @Override
    public BigDecimal postCalcTotalCost(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException {
        return paymentService.calculateTotalCost(orderDto);
    }

    @Override
    public void postSuccessfulPayment(UUID paymentId) throws NoOrderFoundException {
        paymentService.receivePayment(paymentId, true);
    }

    @Override
    public BigDecimal postCalcProductsCostInOrder(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException {
        return paymentService.calculateProductsCost(orderDto);
    }

    @Override
    public void postFailedPayment(UUID paymentId) throws NoOrderFoundException {
        paymentService.receivePayment(paymentId, false);
    }
}