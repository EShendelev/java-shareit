package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item save(Item item, long userId);

    Item update(Long userId, Long itemId, Item item);

    Item get(Long userId, Long itemId);

    Collection<Item> getAllById(Long userId);

    Collection<Item> getItemByText(String request);

    void delete(Long userId, Long itemId);
}
