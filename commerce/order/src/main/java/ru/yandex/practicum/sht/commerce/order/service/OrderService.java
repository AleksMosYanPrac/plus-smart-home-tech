package ru.yandex.practicum.sht.commerce.order.service;

import ru.yandex.practicum.sht.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.order.OrderState;
import ru.yandex.practicum.sht.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto addNewOrder(CreateNewOrderRequest request) throws NoSpecifiedProductInWarehouseException;

    OrderDto addReturnOrder(ProductReturnRequest request) throws NoOrderFoundException;

    OrderDto calculateTotalPrice(UUID orderId) throws NoOrderFoundException;

    OrderDto calculateDeliveryPrice(UUID orderId) throws NoOrderFoundException;

    OrderDto changeOrderState(UUID orderId, OrderState state) throws NoOrderFoundException;

    List<OrderDto> getUserOrders(String username) throws NotAuthorizedUserException;
}