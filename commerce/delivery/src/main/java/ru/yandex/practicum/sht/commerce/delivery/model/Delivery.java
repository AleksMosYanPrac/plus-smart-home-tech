package ru.yandex.practicum.sht.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryState;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue
    private UUID deliveryId;
    private UUID orderId;
    @ManyToOne
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address fromAddress;
    @ManyToOne
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address toAddress;
    @Enumerated
    private DeliveryState deliveryState;

    @Setter
    @Getter
    @Entity
    @Table(name = "addresses")
    public static class Address {

        @Id
        @GeneratedValue
        private UUID addressId;
        private String country;
        private String city;
        private String street;
        private String house;
        private String flat;
    }
}