package ru.yandex.practicum.sht.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "bookings")
public class OrderBooking {

    @Id
    @GeneratedValue
    private UUID orderBookingId;
    private UUID orderId;
    private UUID deliveryId;

    @ElementCollection
    @CollectionTable(name = "order_booking_products", joinColumns = @JoinColumn(name = "order_booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;
}