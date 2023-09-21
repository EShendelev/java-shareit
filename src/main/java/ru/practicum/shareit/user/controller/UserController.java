package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.interfaces.UserService;
import ru.practicum.shareit.validation_mark.Create;
import ru.practicum.shareit.validation_mark.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAll() {
        Collection<User> users = userService.getAll();
        Collection<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = UserMapper.toDto(user);
            userDtos.add(userDto);
        }
        log.info("Вывод списка всех пользователей");
        return userDtos;
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") long id) {
        User user = userService.get(id);
        log.info("Получен пользовать ID {}", id);
        return UserMapper.toDto(user);
    }

    @PostMapping
    public UserDto create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        User user = userService.create(UserMapper.toModel(userDto, null));
        log.info("Создан пользовать ID {}", user.getId());
        return UserMapper.toDto(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@NotNull(message = "поле ID не может быть пустым")
                          @Min(1)
                          @PathVariable("id") long id,
                          @Validated({Update.class})
                          @RequestBody UserDto userDto) {
        User user = userService.update(UserMapper.toModel(userDto, id));
        log.info("Обновлена информация пользователя ID {}", id);
        return UserMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        userService.delete(id);
        log.info("Удален пользователь ID {}", id);
    }
}
