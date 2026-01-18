package ru.yandex.practicum.sht.commerce.payment.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    private UUID paymentId;
    private UUID orderId;
    private BigDecimal totalPayment;
    private BigDecimal deliveryTotal;
    private BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}