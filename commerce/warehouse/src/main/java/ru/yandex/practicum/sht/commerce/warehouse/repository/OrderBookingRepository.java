package ru.yandex.practicum.sht.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.warehouse.model.OrderBooking;

import java.util.UUID;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {
    OrderBooking findByOrderId(UUID orderId);
}