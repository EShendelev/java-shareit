package ru.practicum.shareit.booking.model;

public enum Status {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    APPROVED,
    CANCELED;

    public static Boolean checkValidStatus(String stateParam) {
        for (Status value : Status.values()) {
            boolean check = value.name().equals(stateParam);
            if (check) {
                return true;
            }
        }
        return false;
    }
}
