package ru.practicum.shareit.request.service.interfaces;

import ru.practicum.shareit.request.dto.RequestItemDto;

import java.util.List;

public interface RequestItemService {

    List<RequestItemDto> findAll(Long id, int from, int size);

    List<RequestItemDto> findAllByUserId(Long userId);

    RequestItemDto findById(Long userId, Long requestId);

    RequestItemDto save(Long userId, RequestItemDto requestItemDto);
}