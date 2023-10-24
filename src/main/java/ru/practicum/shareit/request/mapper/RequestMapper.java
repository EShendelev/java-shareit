package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.ArrayList;


public class RequestMapper {

    public static Request toModel(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        return request;
    }

    public static RequestDto toDto(Request request) {
        RequestDto requestDto = new requestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setItems(new ArrayList<>());
        return itemRequestDto;
    }
}