package ru.practicum.shareit.item.repository.memory.util;

import org.springframework.stereotype.Component;

@Component
public class ItemIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
