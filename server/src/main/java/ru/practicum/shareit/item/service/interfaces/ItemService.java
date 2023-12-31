package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemResponseDto> findAllByOwnerId(Long userId, int from, int size);

    Collection<ItemResponseDto> findItemByText(String request, int from, int size);

    ItemResponseDto save(ItemRequestDto itemRequestDto, long userId);

    ItemResponseDto update(Long userId, Long itemId, ItemRequestDto item);

    ItemResponseDto findById(Long userId, Long itemId);

    void delete(Long userId, Long itemId);
}
