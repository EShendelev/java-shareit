package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.implement.CommentServiceImpl;
import ru.practicum.shareit.item.service.interfaces.CommentService;
import ru.practicum.shareit.user.repository.UserRepository;

public class CommentServiceMock {
    CommentService commentService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        commentService = new CommentServiceImpl(
                commentRepository,
                itemRepository,
                userRepository,
                bookingRepository
        );
    }
}
