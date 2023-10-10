package ru.practicum.shareit.exception;

public class BookingsNotFoundException extends RuntimeException {
    public BookingsNotFoundException(String message) {
        super(message);
    }
}
