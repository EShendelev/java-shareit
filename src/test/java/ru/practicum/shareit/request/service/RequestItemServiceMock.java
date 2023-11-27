package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.mapper.RequestItemMapper;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.request.service.implement.RequestItemServiceImpl;
import ru.practicum.shareit.request.service.interfaces.RequestItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RequestItemServiceMock {
    RequestItemService requestItemService;
    RequestItemRepository requestItemRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        requestItemRepository = Mockito.mock(RequestItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        requestItemService = new RequestItemServiceImpl(userRepository, itemRepository, requestItemRepository);
    }

    @Test
    void findAllByUserIdTest() {
        User user = new User(1L, "testName", "test@mail.com");

        List<RequestItem> requests = List.of(
                new RequestItem(1L, "testDescription", user, LocalDateTime.now()));

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestItemRepository.findAllByRequestorIdOrderByCreatedAsc(Mockito.anyLong()))
                .thenReturn(requests);

        Mockito.when(itemRepository.findAllByRequestIn(requests))
                .thenReturn(Collections.emptyList());

    }

    @Test
    void findAllByUserIdTestWithWrongUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestItemService.findAllByUserId(1L)
        );
        Assertions.assertEquals("Пользователь ID 1 не найден", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        User user = new User(1L, "testName", "test@mail.com");
        RequestItem request = new RequestItem(1L, "testDescription", user, LocalDateTime.now());
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestItemRepository.findById(request.getId()))
                .thenReturn(Optional.of(request));

        Mockito.when(itemRepository.findAllByRequestId(request.getId()))
                .thenReturn(Collections.emptyList());

    }

    @Test
    void findByIdTestWithWrongUserId() {
        User user = new User(1L, "testName", "test@mail.com");
        RequestItem request = new RequestItem(1L, "testDescription", user, LocalDateTime.now());

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestItemService.findById(user.getId(), request.getId()));

        Assertions.assertEquals("Пользователь ID 1 не найден", exception.getMessage());
    }

    @Test
    void findByIdTestWithWrongRequestId() {
        User user = new User(1L, "testName", "test@mail.com");
        RequestItem request = new RequestItem(1L, "testDescription", user, LocalDateTime.now());

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestItemRepository.findById(request.getId()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestItemService.findById(user.getId(), request.getId()));

        Assertions.assertEquals("Пользователь ID 1 не найден", exception.getMessage());
    }

    @Test
    void saveTest() {
        RequestItemDto itemRequestDto = new RequestItemDto(null, "testDescription", null, null);

        User user = new User(1L, "test", "test@gmail.com");

        RequestItem itemRequest = RequestItemMapper.toModel(itemRequestDto);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestItemRepository.save(Mockito.any()))
                .thenReturn(itemRequest);

        RequestItemDto request = requestItemService.save(user.getId(), itemRequestDto);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(itemRequestDto.getDescription(), request.getDescription());
    }

    @Test
    void saveTestWithWrongUserId() {
        RequestItemDto itemRequestDto = new RequestItemDto(null, "testDescription", null, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestItemService.save(1L, itemRequestDto)
        );
        Assertions.assertEquals("Пользователь ID 1 не найден", exception.getMessage());
    }
}
