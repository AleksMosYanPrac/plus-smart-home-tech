package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartFeignClient extends ShoppingCartContract{
}