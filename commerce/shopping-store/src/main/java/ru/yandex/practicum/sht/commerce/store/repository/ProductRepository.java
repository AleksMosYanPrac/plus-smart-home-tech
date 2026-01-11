package ru.yandex.practicum.sht.commerce.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.commerce.dto.store.ProductCategory;
import ru.yandex.practicum.sht.commerce.store.model.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);
}