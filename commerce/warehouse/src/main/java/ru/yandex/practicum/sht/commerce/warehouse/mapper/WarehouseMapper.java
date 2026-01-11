package ru.yandex.practicum.sht.commerce.warehouse.mapper;

import ru.yandex.practicum.sht.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.warehouse.config.AddressConfig;
import ru.yandex.practicum.sht.commerce.warehouse.model.Product;

public interface WarehouseMapper {
    Product toProduct(NewProductInWarehouseRequest request);

    AddressDto toAddressDto(AddressConfig.Address address);
}