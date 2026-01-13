package ru.yandex.practicum.sht.commerce.order.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.order.model.Order;

@Component
public class OrderMapperImpl implements OrderMapper {
    @Override
    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setShoppingCartId(order.getShoppingCartId());
        orderDto.setProducts(order.getProducts());
        orderDto.setPaymentId(order.getPaymentId());
        orderDto.setDeliveryId(order.getDeliveryId());
        orderDto.setState(order.getState());
        orderDto.setDeliveryWeight(order.getDeliveryWeight());
        orderDto.setDeliveryVolume(order.getDeliveryVolume());
        orderDto.setFragile(order.getFragile());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setDeliveryPrice(order.getDeliveryPrice());
        orderDto.setProductPrice(order.getProductPrice());
        return orderDto;
    }
}