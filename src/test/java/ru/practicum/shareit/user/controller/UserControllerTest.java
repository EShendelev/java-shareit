package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserController userController;

    private UserDto user;

    @BeforeEach
    void init() {
        user = new UserDto(null, "testName", "test@mail.com");
    }

    @Test
    void getTestWithWrongUserId() {
        assertThrows(NotFoundException.class,
                () -> userController.get(1L));
    }

    @Test
    void saveTest() {
        UserDto userDto = userController.save(user);
        assertEquals(userDto.getId(), userController.get(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.save(user);
        UserDto userDto = new UserDto(null, "testNameUpdate", "testUpdate@mail.com");
        userController.update(1L, userDto);
        assertEquals(userDto.getEmail(), userController.get(1L).getEmail());
    }

    @Test
    void updateTestWithWrongUserId() {
        assertThrows(NotFoundException.class,
                () -> userController.update(1L, user));
    }

    @Test
    void deleteTest() {
        UserDto userDto = userController.save(user);
        assertEquals(1, userController.getAll().size());
        userController.delete(userDto.getId());
        assertEquals(0, userController.getAll().size());
    }

}
