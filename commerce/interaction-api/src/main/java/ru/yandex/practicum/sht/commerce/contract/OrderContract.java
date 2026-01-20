package ru.yandex.practicum.sht.commerce.contract;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.sht.commerce.dto.ApiError;
import ru.yandex.practicum.sht.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public interface OrderContract {

    @GetMapping
    List<OrderDto> getUserOrders(@RequestParam String username) throws NotAuthorizedUserException;

    @PutMapping
    OrderDto putNewOrder(@RequestBody CreateNewOrderRequest request) throws NoSpecifiedProductInWarehouseException;

    @PostMapping("/return")
    OrderDto postReturnOrder(@RequestBody ProductReturnRequest request) throws NoOrderFoundException;

    @PostMapping("/payment")
    OrderDto postOrderPayment(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/payment/failed")
    OrderDto postOrderPaymentWithFail(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/delivery")
    OrderDto postOrderDelivery(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/delivery/failed")
    OrderDto postOrderDeliveryWithFail(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/completed")
    OrderDto postOrderComplete(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/calculate/total")
    OrderDto postCalcOrderTotalPrice(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/calculate/delivery")
    OrderDto postCalcOrderDeliveryPrice(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/assembly")
    OrderDto postAssembleOrder(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/assembly/failed")
    OrderDto postAssembleOrderWithFail(@RequestBody UUID orderId) throws NoOrderFoundException;

    @ExceptionHandler(NotAuthorizedUserException.class)
    default ApiError onNotAuthorizedUser(NotAuthorizedUserException exception) {
        return ApiError.from(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    default ApiError onNotAuthorizedUser(NoSpecifiedProductInWarehouseException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(NoOrderFoundException.class)
    default ApiError onNoOrderFound(NoOrderFoundException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    default ApiError onConstraintViolation(ConstraintViolationException exception) {
        return ApiError.from(exception, BAD_REQUEST);
    }
}