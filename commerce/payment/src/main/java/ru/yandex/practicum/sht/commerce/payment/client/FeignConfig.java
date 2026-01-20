package ru.yandex.practicum.sht.commerce.payment.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.sht.commerce.contract.OrderFeignClient;
import ru.yandex.practicum.sht.commerce.contract.ShoppingStoreFeignClient;

@Configuration
@EnableFeignClients(clients = {ShoppingStoreFeignClient.class, OrderFeignClient.class})
public class FeignConfig {
}
