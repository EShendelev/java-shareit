package ru.practicum.shareit.item.repository.in_memory;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerFailException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.repository.in_memory.util.ItemIdProvider;

import java.util.*;

public class ItemDao implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private final ItemIdProvider idProvider = new ItemIdProvider();

    @Override
    public Item create(Item item) {
        long id = idProvider.getIncrementId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item update(Item forUpdate) {
        long id = forUpdate.getId();
        checkIdItem(id);
        Item updatable = items.get(id);
        checkItemOwner(updatable, forUpdate);

        if (forUpdate.getName() != null) {
            updatable.setName(forUpdate.getName());
        }

        if (forUpdate.getDescription() != null) {
            updatable.setDescription(forUpdate.getDescription());
        }

        if (!Objects.equals(forUpdate.isAvailable(), updatable.isAvailable())) {
            updatable.setAvailable(forUpdate.isAvailable());
        }
        return updatable;
    }

    @Override
    public void delete(long itemId) {
        checkIdItem(itemId);
        items.remove(itemId);
    }

    @Override
    public Item get(long itemId) {
        checkIdItem(itemId);
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }

    @Override
    public Collection<Item> getItemByNameOrDescription(String text) {
        String request = text.toLowerCase();
        List<Item> resultList = new ArrayList<>();

        for (Item item : items.values()) {
            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();

            boolean nameMatch = name.contains(request);
            boolean descriptionMatch = description.contains(request);
            if (nameMatch || descriptionMatch) {
                resultList.add(item);
            }
        }
        return resultList;
    }

    private void checkIdItem(long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException(String.format("Не найден предмет с ID %s", id));
        }
    }

    private void checkItemOwner(Item before, Item after) {
        if (!(before.getOwner().getId() == after.getOwner().getId())) {
            throw new ItemOwnerFailException(String.format("Пользователь с ID %d не является владельцем предмета " +
                    "ID %d", after.getOwner().getId(), before.getId()));
        }
    }
}
