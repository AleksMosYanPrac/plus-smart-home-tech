package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeignClient extends DeliveryContract{
}