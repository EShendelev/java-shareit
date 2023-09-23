package ru.practicum.shareit.user.repository.memory.util;

import org.springframework.stereotype.Component;

@Component
public class UserIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
