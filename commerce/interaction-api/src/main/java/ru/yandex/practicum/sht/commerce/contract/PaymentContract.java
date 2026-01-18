package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.dto.ApiError;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NotEnoughInfoInOrderToCalculateException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public interface PaymentContract {

    @PostMapping
    PaymentDto postPaymentRequest(@RequestBody OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/totalCost")
    BigDecimal postCalcTotalCost(@RequestBody OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/refund")
    void postSuccessfulPayment(@RequestBody UUID paymentId) throws NoOrderFoundException;

    @PostMapping("/productCost")
    BigDecimal postCalcProductsCostInOrder(@RequestBody OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/failed")
    void postFailedPayment(@RequestBody UUID paymentId) throws NoOrderFoundException;

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    default ApiError onNotEnoughInfoInOrder(NotEnoughInfoInOrderToCalculateException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(NoOrderFoundException.class)
    default ApiError onNoOrderFound(NoOrderFoundException exception) {
        return ApiError.from(exception, NOT_FOUND);
    }
}