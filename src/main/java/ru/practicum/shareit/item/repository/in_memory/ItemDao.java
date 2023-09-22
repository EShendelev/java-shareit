package ru.practicum.shareit.item.repository.in_memory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerFailException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.repository.in_memory.util.ItemIdProvider;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemDao implements ItemStorage {

    private final UserStorage userStorage;
    private final Map<Long, Item> items = new HashMap<>();
    private final ItemIdProvider idProvider = new ItemIdProvider();

    @Override
    public Item create(Item item, long ownerId) {
        userStorage.checkUser(ownerId);
        long id = idProvider.getIncrementId();
        item.setId(id);
        item.setOwnerId(ownerId);
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

        if (forUpdate.getAvailable() != null) {
            if (!Objects.equals(forUpdate.getAvailable(), updatable.getAvailable())) {

                updatable.setAvailable(forUpdate.getAvailable());
            }
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
    public Collection<Item> getAllUserItems(Long ownerId) {
        userStorage.checkUser(ownerId);
        Collection<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == ownerId) {
                userItems.add(item);
            }
        }
        return userItems;
    }

    @Override
    public Collection<Item> getItemByNameOrDescription(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        String request = text.toLowerCase();
        List<Item> resultList = new ArrayList<>();

        for (Item item : items.values()) {
            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();

            boolean nameMatch = name.contains(request);
            boolean descriptionMatch = description.contains(request);
            boolean isAvailable = item.getAvailable();
            if ((nameMatch || descriptionMatch) && isAvailable) {
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
        if (!(before.getOwnerId() == after.getOwnerId())) {
            throw new ItemOwnerFailException(String.format("Пользователь с ID %d не является владельцем предмета " +
                    "ID %d", after.getOwnerId(), before.getId()));
        }
    }
}
