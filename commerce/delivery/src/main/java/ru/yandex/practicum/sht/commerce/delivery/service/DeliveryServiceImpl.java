package ru.yandex.practicum.sht.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.sht.commerce.contract.OrderFeignClient;
import ru.yandex.practicum.sht.commerce.contract.WarehouseFeignClient;
import ru.yandex.practicum.sht.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.sht.commerce.delivery.model.Delivery;
import ru.yandex.practicum.sht.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryState;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.sht.commerce.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryState.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderFeignClient orderClient;
    private final WarehouseFeignClient warehouseClient;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper mapper;

    @Override
    public DeliveryDto addNewDelivery(DeliveryDto deliveryDto) {
        log.info("Add new delivery for order:{}", deliveryDto.getOrderId());
        Delivery newDelivery = new Delivery();
        newDelivery.setOrderId(deliveryDto.getOrderId());
        newDelivery.setFromAddress(mapper.toAddress(deliveryDto.getFromAddress()));
        newDelivery.setToAddress(mapper.toAddress(deliveryDto.getToAddress()));
        newDelivery.setDeliveryState(CREATED);
        return mapper.toDto(deliveryRepository.save(newDelivery));
    }

    @Override
    public void changeDeliveryState(UUID deliveryId, DeliveryState deliveryState) throws NoDeliveryFoundException {
        log.info("Change state for delivery:{}, status:{}", deliveryId, deliveryState);
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Not found delivery:" + deliveryId));
        switch (deliveryState) {
            case FAILED -> orderClient.postOrderDeliveryWithFail(delivery.getOrderId());
            case IN_PROGRESS -> {
                orderClient.postAssembleOrder(delivery.getOrderId());
                warehouseClient.postTransferProductsToDelivery(
                        new ShippedToDeliveryRequest(delivery.getOrderId(), delivery.getDeliveryId()));
            }
            case DELIVERED -> orderClient.postOrderDelivery(delivery.getOrderId());
        }
        delivery.setDeliveryState(deliveryState);
        deliveryRepository.save(delivery);
    }

    @Override
    public BigDecimal calculateDeliveryPrice(OrderDto orderDto) throws NoDeliveryFoundException {
        log.info("Calculate delivery price for order:{}", orderDto.getOrderId());
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found for order:" + orderDto.getOrderId()));
        BigDecimal baseCost = BigDecimal.valueOf(5);
        BigDecimal price;
        price = caseWarehouseAddress(baseCost, delivery.getFromAddress());
        price = price.add(caseFragile(price, orderDto.getFragile()));
        price = price.add(BigDecimal.valueOf(orderDto.getDeliveryWeight() * 0.3));
        price = price.add(BigDecimal.valueOf(orderDto.getDeliveryVolume() * 0.2));
        price = price.add(caseDeliveryAddress(price, delivery.getFromAddress(), delivery.getToAddress()));
        return price;
    }

    private BigDecimal caseDeliveryAddress(BigDecimal cost, Delivery.Address fromAddress, Delivery.Address toAddress) {
        if (fromAddress.getStreet().equalsIgnoreCase(toAddress.getStreet())) {
            return BigDecimal.ZERO;
        }
        return cost.multiply(BigDecimal.valueOf(0.2));
    }

    private BigDecimal caseFragile(BigDecimal cost, Boolean fragile) {
        if (fragile) {
            return cost.multiply(BigDecimal.valueOf(0.2));
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal caseWarehouseAddress(BigDecimal baseCost, Delivery.Address warehouseAddress) {
        if (warehouseAddress.getStreet().equalsIgnoreCase("ADDRESS_1")) {
            return baseCost;
        }
        return baseCost.add(baseCost.multiply(BigDecimal.valueOf(2)));
    }
}