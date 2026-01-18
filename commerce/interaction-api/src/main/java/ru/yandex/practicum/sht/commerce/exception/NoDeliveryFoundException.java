package ru.yandex.practicum.sht.commerce.exception;

public class NoDeliveryFoundException extends RuntimeException {
    public NoDeliveryFoundException(String message) {
        super(message);
    }
}
