package ru.yandex.practicum.sht.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.warehouse.model.Product;

import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Product, UUID> {
}