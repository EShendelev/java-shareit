package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.dto.CommentDto;

public interface CommentService {
    CommentDto save(Long userId, Long itemId, CommentDto commentDto);
}
