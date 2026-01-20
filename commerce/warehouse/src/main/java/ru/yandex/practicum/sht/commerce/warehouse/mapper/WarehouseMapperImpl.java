package ru.yandex.practicum.sht.commerce.warehouse.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.DimensionDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.sht.commerce.warehouse.config.AddressConfig;
import ru.yandex.practicum.sht.commerce.warehouse.model.Product;

@Component
public class WarehouseMapperImpl implements WarehouseMapper {
    @Override
    public Product toProduct(NewProductInWarehouseRequest request) {
        Product product = new Product();
        product.setProductId(request.getProductId());
        product.setFragile(request.getFragile());
        product.setDimension(toDimension(request.getDimension()));
        product.setWeight(request.getWeight());
        product.setQuantity(0L);
        return product;
    }

    private Product.Dimension toDimension(DimensionDto dimensionDto) {
        Product.Dimension dimension = new Product.Dimension();
        dimension.setDepth(dimensionDto.getDepth());
        dimension.setWidth(dimensionDto.getWidth());
        dimension.setHeight(dimensionDto.getHeight());
        return dimension;
    }

    @Override
    public AddressDto toAddressDto(AddressConfig.Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry(address.getAddress());
        addressDto.setCity(address.getAddress());
        addressDto.setStreet(address.getAddress());
        addressDto.setHouse(address.getAddress());
        addressDto.setFlat(address.getAddress());
        return addressDto;
    }
}
