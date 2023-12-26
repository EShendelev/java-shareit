package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.validmark.Create;
import ru.practicum.shareit.validmark.Update;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Gateway user controller: get all Users");
        return userClient.getUsers();
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        log.info("Gateway user controller: get User ID {}.", userId);
        return userClient.get(userId);
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @Validated(Create.class)
            @RequestBody UserDtoRequest userDtoRequest) {
        log.info("Gateway user controller: create User");
        return userClient.save(userDtoRequest);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> update(
            @PathVariable Long userId,
            @Validated(Update.class)
            @RequestBody UserDtoRequest userDtoRequest) {
        log.info("Gateway user controller: update User ID {}.", userId);
        return userClient.update(userId, userDtoRequest);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Gateway user controller: delete User ID {}.", userId);
        return userClient.delete(userId);
    }
}
