package ru.practicum.shareit.user.repository.in_memory.util;

public class UserIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
