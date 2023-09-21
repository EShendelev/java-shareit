package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item create(Item item, long ownerId);

    Item update(Item item);

    Item get(long id);

    Collection<Item> getAll();

    Collection<Item> getItemByNameOrDescription(String request);
}
