package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestItemClient;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.validmark.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
@Slf4j
@Validated
public class RequestItemController {
    private final RequestItemClient requestItemClient;

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Gateway item request: get Item Requests By User ID {}.", userId);
        return requestItemClient.findAllByUserId(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> findAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("Gateway item request: get All Item Requests User ID {}.", userId);
        return requestItemClient.findAll(userId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        log.info("Gateway item request: get Item Request User ID {}, request ID {}.",
                userId, requestId);
        return requestItemClient.findById(requestId, userId);
    }


    @PostMapping
    public ResponseEntity<Object> save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class)
            @RequestBody RequestItemDto requestDto) {
        log.info("Gateway item request: create Item Request User ID {}.", userId);
        return requestItemClient.save(userId, requestDto);
    }
}
