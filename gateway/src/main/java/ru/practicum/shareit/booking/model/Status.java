package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.StatusNotExistException;

import java.util.Optional;

public enum Status {
    // Все
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Завершенные
    PAST,
    // Отклоненные
    REJECTED,
    // Ожидающие подтверждения
    WAITING;

    public static Optional<Status> from(String stringState) {
        for (Status state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

    public static Status checkValidStatus(String stateParam) {
        try {
            Status.valueOf(stateParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StatusNotExistException("Unknown state: " + stateParam);
        }
        return Status.valueOf(stateParam);

    }
}