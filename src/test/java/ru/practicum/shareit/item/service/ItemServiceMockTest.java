package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.implement.ItemServiceImpl;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public class ItemServiceMockTest {
    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    RequestItemRepository requestItemRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        requestItemRepository = Mockito.mock(RequestItemRepository.class);
        itemService = new ItemServiceImpl(
                commentRepository,
                itemRepository,
                userRepository,
                bookingRepository,
                requestItemRepository
        );
    }

    @Test
    void findAllByTextTestWithEmptyList() {
        User user = new User(1L, "testName", "test@mail.com");
        String text = "";

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Collection<ItemResponseDto> results = itemService.findItemByText(text, 0, 10);

        Assertions.assertEquals(0, results.size());
    }

    @Test
    void saveTest() {
        User user = new User(1L, "testName", "test@mail.com");
        Item item = new Item(1L, "testName", "testDescription", true, user, null);
        ItemRequestDto itemDto =
                new ItemRequestDto(null, "testName", "testDescription", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemResponseDto foundItem = itemService.save(itemDto, user.getId());

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void saveTestWithWrongUserId() {
        ItemRequestDto itemDto =
                new ItemRequestDto(null, "testName", "testDescription", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> itemService.save(itemDto, 1L));

        Assertions.assertEquals("Пользователь  ID 1 не найден", exception.getMessage());
    }

    @Test
    void saveTestWithRequest() {
        User user1 = new User(1L, "testName1", "test1@mail.com");
        User user2 = new User(2L, "testName2", "test2@mail.com");
        RequestItem itemRequest =
                new RequestItem(1L, "testDescription", user2, LocalDateTime.now());
        Item item =
                new Item(1L, "testName", "testDescription", true, user1, itemRequest);
        ItemRequestDto itemDto =
                new ItemRequestDto(null, "testName", "testDescription", true, 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito.when(requestItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemResponseDto foundItem = itemService.save(itemDto, user1.getId());

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void saveTestWithWrongRequestId() {
        User user = new User(1L, "testName", "test@mail.com");
        ItemRequestDto itemDto =
                new ItemRequestDto(null, "testName", "testDescription", true, 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> itemService.save(itemDto, 1L));

        Assertions.assertEquals("Запрос ID 1 не найден", exception.getMessage());
    }

    @Test
    void updateTest() {
        User user = new User(1L, "testName", "test@mail.com");
        Item item = new Item(1L, "testName", "testDescription", true, user, null);
        Item itemUpdate =
                new Item(1L, "testNameUpdate", "testDescriptionUpdate",
                        true, user, null);
        ItemRequestDto itemDto =
                new ItemRequestDto(null, "testNameUpdate", "testDescriptionUpdate",
                        true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemUpdate);

        ItemResponseDto foundItem = itemService.update(user.getId(), item.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }
}
