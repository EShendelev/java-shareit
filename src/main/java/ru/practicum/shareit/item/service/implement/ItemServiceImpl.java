package ru.practicum.shareit.item.service.implement;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.interfaces.ItemService;

import java.util.Collection;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemStorage itemStorage;

    @Override
    public Item create(Item item, long ownerId) {
        return itemStorage.create(item, ownerId);
    }

    @Override
    public Item update(Item item) {
        return itemStorage.update(item);
    }

    @Override
    public Item get(long id) {
        return itemStorage.get(id);
    }

    @Override
    public Collection<Item> getAll() {
        return itemStorage.getAll();
    }

    @Override
    public Collection<Item> getItemByNameOrDescription(String text) {
        return itemStorage.getItemByNameOrDescription(text);
    }
}
