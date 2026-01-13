package ru.yandex.practicum.sht.commerce.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.sht.commerce.dto.order.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_quantity")
    private Map<UUID, Long> products;

    private UUID paymentId;
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;

    private BigDecimal totalPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal productPrice;
}