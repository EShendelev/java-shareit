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

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("ItemRequestGatewayController: getAllItemRequests implementation. User ID {}.", userId);
        return requestItemClient.getAll(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestGatewayController: getItemRequestsByUser implementation. User ID {}.", userId);
        return requestItemClient.getItemRequestsByUser(userId);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        log.info("ItemRequestGatewayController: getItemRequest implementation. User ID {}, request ID {}.",
                userId, requestId);
        return requestItemClient.getItemRequest(requestId, userId);
    }


    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class)
            @RequestBody RequestItemDto requestDto) {
        log.info("ItemRequestGatewayController: createItemRequest implementation. User ID {}.", userId);
        return requestItemClient.createItemRequest(userId, requestDto);
    }
}
