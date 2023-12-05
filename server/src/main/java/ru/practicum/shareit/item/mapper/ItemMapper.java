package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

@Component
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
        itemResponseDto.setLastBooking(null);
        itemResponseDto.setNextBooking(null);
        itemResponseDto.setComments(new ArrayList<>());
        itemResponseDto.setRequestId(item.getRequest() == null ? null : item.getRequest().getId());
        return itemResponseDto;
    }

    public static Item toModel(ItemRequestDto itemRequestDto) {
        Item item = new Item();
        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setAvailable(itemRequestDto.getAvailable());
        item.setDescription(itemRequestDto.getDescription());
        return item;
    }
}
