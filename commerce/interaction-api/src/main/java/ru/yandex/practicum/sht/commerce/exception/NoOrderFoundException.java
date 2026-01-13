package ru.yandex.practicum.sht.commerce.exception;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}