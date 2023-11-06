package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestItemControllerTest {
    @Autowired
    private RequestItemController requestItemController;

    @Autowired
    private UserController userController;

    private RequestItemDto itemRequestDto;

    private UserDto userDto1;

    private UserDto userDto2;

    @BeforeEach
    void init() {
        itemRequestDto = new RequestItemDto(null, "testDescription", null, null);

        userDto1 = new UserDto(null, "testName1", "test1@mail.com");

        userDto2 = new UserDto(null, "testName2", "test2@mail.com");
    }

    @Test
    void findAllTest() {
        UserDto user = userController.save(userDto1);
        requestItemController.save(user.getId(), itemRequestDto);
        assertEquals(0, requestItemController.findAll(user.getId(), 0, 10).size());
        UserDto user2 = userController.save(userDto2);
        assertEquals(1, requestItemController.findAll(user2.getId(), 0, 10).size());
    }

    @Test
    void findAllTestWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> requestItemController
                .findAll(1L, 0, 10));
    }

    @Test
    void findAllTestWithWrongFrom() {
        assertThrows(ConstraintViolationException.class, () -> requestItemController
                .findAll(1L, -1, 10));
    }

    @Test
    void findAllByUserIdTest() {
        UserDto user = userController.save(userDto1);
        requestItemController.save(user.getId(), itemRequestDto);
        assertEquals(1, requestItemController.findAllByUserId(user.getId()).size());
    }

    @Test
    void findAllByUserIdTestWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> requestItemController.findAllByUserId(1L));
    }

    @Test
    void saveTest() {
        UserDto user = userController.save(userDto1);
        RequestItemDto itemRequest = requestItemController.save(user.getId(), itemRequestDto);
        assertEquals(1L, requestItemController.findById(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void saveTestWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> requestItemController.save(1L, itemRequestDto));
    }
    
}
