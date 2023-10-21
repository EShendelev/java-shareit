package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.StatusNotExistException;

public enum Status {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    APPROVED,
    CANCELED;

    public static void checkValidStatus(String stateParam) {
        try {
            Status.valueOf(stateParam);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new StatusNotExistException("Unknown state: " + stateParam);
        }
    }
}
