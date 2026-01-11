package ru.yandex.practicum.sht.commerce.store.mapper;

import ru.yandex.practicum.sht.commerce.dto.store.ProductDto;
import ru.yandex.practicum.sht.commerce.store.model.Product;

public interface ProductMapper {

    Product toProduct(ProductDto productDto);

    ProductDto toDto(Product product);
}