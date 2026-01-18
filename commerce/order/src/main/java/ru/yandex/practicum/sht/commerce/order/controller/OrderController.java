package ru.yandex.practicum.sht.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.sht.commerce.contract.OrderContract;
import ru.yandex.practicum.sht.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.order.OrderState;
import ru.yandex.practicum.sht.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;
import ru.yandex.practicum.sht.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderContract {

    private final OrderService orderService;

    @Override
    public List<OrderDto> getUserOrders(String username) throws NotAuthorizedUserException {
        return orderService.getUserOrders(username);
    }

    @Override
    public OrderDto putNewOrder(CreateNewOrderRequest request) throws NoSpecifiedProductInWarehouseException {
        return orderService.addNewOrder(request);
    }

    @Override
    public OrderDto postReturnOrder(ProductReturnRequest request) throws NoOrderFoundException {
        return orderService.addReturnOrder(request);
    }

    @Override
    public OrderDto postOrderPayment(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.PAID);
    }

    @Override
    public OrderDto postOrderPaymentWithFail(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto postOrderDelivery(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.DELIVERED);
    }

    @Override
    public OrderDto postOrderDeliveryWithFail(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto postOrderComplete(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.DONE);
    }

    @Override
    public OrderDto postCalcOrderTotalPrice(UUID orderId) throws NoOrderFoundException {
        return orderService.calculateTotalPrice(orderId);
    }

    @Override
    public OrderDto postCalcOrderDeliveryPrice(UUID orderId) throws NoOrderFoundException {
        return orderService.calculateDeliveryPrice(orderId);
    }

    @Override
    public OrderDto postAssembleOrder(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.ASSEMBLED);
    }

    @Override
    public OrderDto postAssembleOrderWithFail(UUID orderId) throws NoOrderFoundException {
        return orderService.changeOrderState(orderId, OrderState.ASSEMBLY_FAILED);
    }
}