package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.model.RequestItem;

import java.util.ArrayList;


public class RequestItemMapper {

    public static RequestItem toModel(RequestItemDto requestItemDto) {
        RequestItem request = new RequestItem();
        if (requestItemDto.getId() != null) {
            request.setId(requestItemDto.getId());
        }
        request.setDescription(requestItemDto.getDescription());
        return request;
    }

    public static RequestItemDto toDto(RequestItem requestItem) {
        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setId(requestItem.getId());
        requestItemDto.setDescription(requestItem.getDescription());
        requestItemDto.setCreated(requestItem.getCreated());
        requestItemDto.setItems(new ArrayList<>());
        return requestItemDto;
    }
}