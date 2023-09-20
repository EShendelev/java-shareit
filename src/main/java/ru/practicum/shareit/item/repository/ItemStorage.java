package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item create(Item item);

    Item update(Item item);

    void delete(long itemId);

    Item get(long itemId);

    Collection<Item> getAll();

    Collection<Item> getItemByNameOrDescription(String text);
}
