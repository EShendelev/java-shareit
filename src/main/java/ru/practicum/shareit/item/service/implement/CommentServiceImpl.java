package ru.practicum.shareit.item.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.interfaces.CommentService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public CommentDto save(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь  ID %d не найден", userId))
        );
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет ID %d не найден", itemId))
        );

        boolean bookingApproved = bookingRepository
                .findByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now())
                .stream()
                .noneMatch(booking -> booking.getStatus().equals(Status.APPROVED));

        if (bookingApproved) {
            throw new ValidateException(String.format("Пользователь ID %d не может забронировать предмет ID %d",
                    userId, itemId));
        }

        Comment comment = CommentMapper.toModel(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment saveComment = commentRepository.save(comment);
        log.debug("CommentService: произведена запись или обновление комментария пользователя ID {}" +
                " и предмета ID {}", userId, itemId);
        return CommentMapper.toDto(saveComment);
    }
}
