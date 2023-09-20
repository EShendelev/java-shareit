package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item create(Item item);

    Item update(Item item);

    Item get(long id);

    Collection<Item> getAll(long userId);

    Collection<Item> getByRequest(String request);
}
