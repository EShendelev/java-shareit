package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemResponseDto toDtoResponse(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        Long ownerId = item.getOwner().getId();
        String ownerName = item.getName();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());
        itemResponseDto.setOwner(new ItemResponseDto.ItemOwner(ownerId, ownerName));
        itemResponseDto.setLastBoking(null);
        itemResponseDto.setNextBoking(null);
        itemResponseDto.setComments(new ArrayList<>());
        return itemResponseDto;
    }

    public static Item toModel(ItemRequestDto itemRequestDto, Long ownerId) {
        Item item = new Item();
        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setAvailable(itemRequestDto.getAvailable());
        item.setDescription(itemRequestDto.getDescription());
        return item;
    }

    public static ItemRequestDto toDto(Item item) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(item.getId());
        itemRequestDto.setName(item.getName());
        itemRequestDto.setDescription(item.getDescription());
        itemRequestDto.setAvailable(item.getAvailable());
        return itemRequestDto;
    }

    public static Item toModelForUpdate(ItemRequestDto itemRequestDto, Long itemId, Long ownerId) {
        Item item = toModel(itemRequestDto, ownerId);
        item.setId(itemId);
        return item;
    }

    public static Collection<ItemRequestDto> toDtoCollection(Collection<Item> items) {
        return items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }
}
