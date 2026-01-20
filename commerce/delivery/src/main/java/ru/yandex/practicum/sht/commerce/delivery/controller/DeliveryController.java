package ru.yandex.practicum.sht.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.sht.commerce.contract.DeliveryContract;
import ru.yandex.practicum.sht.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryState;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryContract {

    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto putAddNewDelivery(DeliveryDto deliveryDto) {
        return deliveryService.addNewDelivery(deliveryDto);
    }

    @Override
    public void postDeliverySuccessful(UUID deliveryId) throws NoDeliveryFoundException {
        deliveryService.changeDeliveryState(deliveryId, DeliveryState.DELIVERED);
    }

    @Override
    public void postDeliveryPicked(UUID deliveryId) throws NoDeliveryFoundException {
        deliveryService.changeDeliveryState(deliveryId, DeliveryState.IN_PROGRESS);
    }

    @Override
    public void postDeliveryFailed(UUID deliveryId) throws NoDeliveryFoundException {
        deliveryService.changeDeliveryState(deliveryId,DeliveryState.FAILED);
    }

    @Override
    public BigDecimal postCalcDeliveryCost(OrderDto orderDto) throws NoDeliveryFoundException {
        return deliveryService.calculateDeliveryPrice(orderDto);
    }
}