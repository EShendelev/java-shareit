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
        log.info("UserGatewayController: getUsers implementation.");
        return userClient.getUsers();
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("UserGatewayController: getUser implementation. User ID {}.", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @Validated(Create.class)
            @RequestBody UserDtoRequest userDtoRequest) {
        log.info("UserGatewayController: createUser implementation.");
        return userClient.createUser(userDtoRequest);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long userId,
            @Validated(Update.class)
            @RequestBody UserDtoRequest userDtoRequest) {
        log.info("UserGatewayController: updateUser implementation. User ID {}.", userId);
        return userClient.updateUser(userId, userDtoRequest);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("UserGatewayController: deleteUser implementation. User ID {}.", userId);
        return userClient.deleteUser(userId);
    }
}
