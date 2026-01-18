package ru.yandex.practicum.sht.commerce.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.delivery.model.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    Optional<Delivery> findByOrderId(UUID orderId);
}