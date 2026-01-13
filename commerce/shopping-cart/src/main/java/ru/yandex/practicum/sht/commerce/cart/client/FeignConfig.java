package ru.yandex.practicum.sht.commerce.cart.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.sht.commerce.contract.WarehouseFeignClient;

@Configuration
@EnableFeignClients(clients = WarehouseFeignClient.class)
public class FeignConfig {
}