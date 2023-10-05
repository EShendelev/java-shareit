package ru.practicum.shareit.item.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.CommentRepository;
import ru.practicum.shareit.item.repository.db.ItemRepository;
import ru.practicum.shareit.item.service.interfaces.CommentService;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Comment save(Long userId, Long itemId, Comment comment) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь  ID %d не найден", userId))
        );
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException(String.format("Предмет ID %d не найден", itemId))
        );

        Comment saveComment = new Comment();
        saveComment.setItem(item);
        saveComment.setAuthor(user);
        saveComment.setCreated(LocalDateTime.now());
        log.debug("CommentService: произведена запись или обновление комментария пользователя ID {}" +
                " и предмета ID {}", userId, itemId);
        return commentRepository.save(saveComment);
    }
}
