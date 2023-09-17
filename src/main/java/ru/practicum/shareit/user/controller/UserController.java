package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @GetMapping
    public Collection<User> getAll() {
        return null;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") long id) {
        return null;
    }

    @PostMapping
    public User create(@Validated UserDto user) {
        return null;
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable("id") long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id")long id) {
    }
}
