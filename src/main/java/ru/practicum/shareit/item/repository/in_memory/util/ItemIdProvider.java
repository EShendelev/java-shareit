package ru.practicum.shareit.item.repository.in_memory.util;

public class ItemIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
