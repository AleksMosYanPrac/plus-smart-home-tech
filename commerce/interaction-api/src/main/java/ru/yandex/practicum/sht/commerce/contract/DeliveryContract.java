package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.sht.commerce.dto.ApiError;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.exception.NoDeliveryFoundException;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public interface DeliveryContract {

    @PutMapping
    DeliveryDto putAddNewDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void postDeliverySuccessful(@RequestBody UUID deliveryId) throws NoDeliveryFoundException;

    @PostMapping("/picked")
    void postDeliveryPicked(@RequestBody UUID deliveryId) throws NoDeliveryFoundException;

    @PostMapping("/failed")
    void postDeliveryFailed(@RequestBody UUID deliveryId) throws NoDeliveryFoundException;

    @PostMapping("/cost")
    BigDecimal postCalcDeliveryCost(@RequestBody OrderDto orderDto) throws NoDeliveryFoundException;

    @ExceptionHandler(NoOrderFoundException.class)
    default ApiError onNoOrderFound(NoOrderFoundException exception) {
        return ApiError.from(exception, NOT_FOUND);
    }
}