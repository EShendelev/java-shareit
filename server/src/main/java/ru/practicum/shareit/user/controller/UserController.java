package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.interfaces.UserService;

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
        log.info("Server user controller: get User ID {}.", id);
        return user;
    }

    @PostMapping
    public UserDto save(@RequestBody UserDto userDto) {
        UserDto user = userService.save(userDto);
        log.info("Server user controller: create User");
        return user;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@NotNull(message = "поле ID не может быть пустым")
                          @PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        UserDto user = userService.update(userId, userDto);
        log.info("Server user controller: update User ID {}.", userId);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        userService.delete(id);
        log.info("Server user controller: delete User ID {}.", id);
    }
}
