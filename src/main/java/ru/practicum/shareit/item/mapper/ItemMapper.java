package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDtoResponse toDtoResponse(Item item) {
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();
        Long ownerId = item.getOwner().getId();
        String ownerName = item.getName();
        itemDtoResponse.setId(item.getId());
        itemDtoResponse.setName(item.getName());
        itemDtoResponse.setDescription(item.getDescription());
        itemDtoResponse.setAvailable(item.getAvailable());
        itemDtoResponse.setOwner(new ItemDtoResponse.ItemOwner(ownerId, ownerName));
        itemDtoResponse.setLastBoking(null);
        itemDtoResponse.setNextBoking(null);
        itemDtoResponse.setComments(new ArrayList<>());
        return itemDtoResponse;
    }

    public static Item toModel(ItemDto itemDto, Long ownerId) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        return item;
    }

    public static ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static Item toModelForUpdate(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = toModel(itemDto, ownerId);
        item.setId(itemId);
        return item;
    }

    public static Collection<ItemDto> toDtoCollection(Collection<Item> items) {
        return items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }
}
