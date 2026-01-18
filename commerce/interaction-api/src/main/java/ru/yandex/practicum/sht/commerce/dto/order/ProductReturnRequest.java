package ru.yandex.practicum.sht.commerce.dto.order;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID,Long> products;
}