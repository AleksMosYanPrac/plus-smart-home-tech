package ru.yandex.practicum.sht.commerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.payment.model.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}