package ru.practicum.shareit.exception;

public class StatusNotExistException extends RuntimeException {
    public StatusNotExistException(String message) {
        super(message);
    }
}
