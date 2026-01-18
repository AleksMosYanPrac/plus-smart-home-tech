package ru.yandex.practicum.sht.commerce.order.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.sht.commerce.contract.DeliveryFeignClient;
import ru.yandex.practicum.sht.commerce.contract.PaymentFeignClient;
import ru.yandex.practicum.sht.commerce.contract.ShoppingCartFeignClient;
import ru.yandex.practicum.sht.commerce.contract.WarehouseFeignClient;

@Configuration
@EnableFeignClients(clients = {
        DeliveryFeignClient.class,
        PaymentFeignClient.class,
        WarehouseFeignClient.class,
        ShoppingCartFeignClient.class
})
public class FeignConfig {
}
