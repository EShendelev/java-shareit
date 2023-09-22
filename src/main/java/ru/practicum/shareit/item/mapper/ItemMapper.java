package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.ArrayList;
import java.util.Collection;

public class ItemMapper {
    private UserStorage userStorage;

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toModelForCreate(ItemDto itemDto, Long ownerId) {
        return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                ownerId, null);
    }

    public static Item toModelForUpdate(ItemDto itemDto, Long itemId, Long ownerId) {
        return new Item(itemId, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                ownerId, null);
    }

    public static Collection<ItemDto> toDtoCollection(Collection<Item> items) {
        Collection<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = toDto(item);
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }
}
