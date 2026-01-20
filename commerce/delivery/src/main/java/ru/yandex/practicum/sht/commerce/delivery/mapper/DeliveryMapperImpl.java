package ru.yandex.practicum.sht.commerce.delivery.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.sht.commerce.delivery.model.Delivery;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.delivery.DeliveryDto;

import static ru.yandex.practicum.sht.commerce.delivery.model.Delivery.*;

@Component
public class DeliveryMapperImpl implements DeliveryMapper {
    @Override
    public Address toAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setHouse(addressDto.getHouse());
        address.setFlat(addressDto.getFlat());
        return address;
    }

    @Override
    public DeliveryDto toDto(Delivery delivery) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryId(delivery.getDeliveryId());
        deliveryDto.setOrderId(delivery.getOrderId());
        deliveryDto.setFromAddress(toAddressDto(delivery.getFromAddress()));
        deliveryDto.setToAddress(toAddressDto(delivery.getToAddress()));
        deliveryDto.setDeliveryState(delivery.getDeliveryState());
        return deliveryDto;
    }

    private AddressDto toAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry(address.getCountry());
        addressDto.setCity(address.getCity());
        addressDto.setStreet(address.getStreet());
        addressDto.setHouse(address.getHouse());
        addressDto.setFlat(address.getFlat());
        return addressDto;
    }
}