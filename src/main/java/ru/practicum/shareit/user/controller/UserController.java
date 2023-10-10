package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.interfaces.UserService;
import ru.practicum.shareit.validmark.Create;
import ru.practicum.shareit.validmark.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
        Collection<UserDto> users = userService.getAll();
        log.info("Вывод списка всех пользователей");
        return users;
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") long id) {
        UserDto user = userService.get(id);
        log.info("Получен пользовать ID {}", id);
        return user;
    }

    @PostMapping
    public UserDto save(@Validated({Create.class}) @RequestBody UserDto userDto) {
        UserDto user = userService.save(userDto);
        log.info("Создан пользовать ID {}", user.getId());
        return user;
    }

    @PatchMapping("/{id}")
    public UserDto update(@NotNull(message = "поле ID не может быть пустым")
                          @Min(1)
                          @PathVariable("id") long id,
                          @Validated({Update.class})
                          @RequestBody UserDto userDto) {
        UserDto user = userService.update(id, userDto);
        log.info("Обновлена информация пользователя ID {}", id);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        userService.delete(id);
        log.info("Удален пользователь ID {}", id);
    }
}
