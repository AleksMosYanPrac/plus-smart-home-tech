package ru.yandex.practicum.sht.commerce.exception;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}