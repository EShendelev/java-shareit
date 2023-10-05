package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.model.Comment;

public interface CommentService {
    Comment save(Long userId, Long itemId, Comment comment);
}
