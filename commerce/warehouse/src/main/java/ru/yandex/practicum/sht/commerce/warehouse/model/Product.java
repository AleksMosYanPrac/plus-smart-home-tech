package ru.yandex.practicum.sht.commerce.warehouse.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    private UUID productId;
    private Long quantity;
    private Boolean fragile;
    private Double weight;

    @Embedded
    private Dimension dimension;

    public void addQuantity(Long count) {
        this.quantity = Math.addExact(quantity, count);
    }

    public double getVolume() {
        return dimension.calcVolume();
    }

    public void reduceQuantity(Long count) {
        this.quantity = Math.subtractExact(quantity, count);
    }

    @Data
    public static class Dimension {
        private Double width;
        private Double height;
        private Double depth;

        public double calcVolume() {
            return width * height * depth;
        }
    }
}
