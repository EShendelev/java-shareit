package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

public interface RequestService {
    
    List<RequestDto> findAll(Long id, int pageNumber, int pageSize);

    List<RequestDto> findAllByUserId(Long userId);

    RequestDto findById(Long userId, Long requesstId);

    RequestDto save(Long userId, RequestDto requestDto);
}