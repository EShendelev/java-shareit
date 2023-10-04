package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDtoRequest toDto(Item item) {
        return ItemDtoRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toModel(ItemDtoRequest itemDtoRequest, Long ownerId) {
        return Item.builder()
                .name(itemDtoRequest.getName())
                .description(itemDtoRequest.getDescription())
                .available(itemDtoRequest.getAvailable())
                .ownerId(ownerId)
                .request(null)
                .build();
    }

    public static Item toModelForUpdate(ItemDtoRequest itemDtoRequest, Long itemId, Long ownerId) {
        Item item = toModel(itemDtoRequest, ownerId);
        item.setId(itemId);
        return item;
    }

    public static Collection<ItemDtoRequest> toDtoCollection(Collection<Item> items) {
        return items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }
}
