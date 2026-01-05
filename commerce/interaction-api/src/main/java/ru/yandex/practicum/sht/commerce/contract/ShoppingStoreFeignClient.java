package ru.yandex.practicum.sht.commerce.contract;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-store",path = "/api/v1/shopping-store")
public interface ShoppingStoreFeignClient extends ShoppingStoreContract {
}