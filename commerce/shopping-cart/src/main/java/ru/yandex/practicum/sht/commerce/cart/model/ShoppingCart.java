package ru.yandex.practicum.sht.commerce.cart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    private String username;

    private boolean isActive;

    @ElementCollection
    @CollectionTable(name = "cart_products", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_quantity")
    private Map<UUID, Long> products = new HashMap<>();

    public void addProducts(Map<UUID, Long> productByQuantity) {
        productByQuantity.forEach((productId, quantity) -> {
            this.products.merge(productId, quantity, Long::sum);
        });
    }

    public void removeProducts(Set<UUID> productList) {
        for (UUID productId : productList) {
            this.products.remove(productId);
        }
    }

    public boolean isContainProducts(Collection<UUID> productList) {
        return this.products.keySet().containsAll(productList);
    }

    public void updateProductQuantity(UUID productId, Long quantity) {
        this.products.put(productId, quantity);
    }
}