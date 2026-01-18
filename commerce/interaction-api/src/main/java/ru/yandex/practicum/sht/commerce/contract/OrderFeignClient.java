package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeignClient extends OrderContract {
}