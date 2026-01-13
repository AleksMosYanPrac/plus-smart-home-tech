package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeignClient extends PaymentContract{
}