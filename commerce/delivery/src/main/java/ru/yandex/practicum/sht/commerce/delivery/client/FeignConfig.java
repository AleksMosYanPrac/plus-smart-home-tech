package ru.yandex.practicum.sht.commerce.delivery.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.sht.commerce.contract.OrderFeignClient;
import ru.yandex.practicum.sht.commerce.contract.WarehouseFeignClient;

@Configuration
@EnableFeignClients(clients = {OrderFeignClient.class, WarehouseFeignClient.class})
public class FeignConfig {
}