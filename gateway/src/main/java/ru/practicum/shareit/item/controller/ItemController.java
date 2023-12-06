package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.validmark.Create;
import ru.practicum.shareit.validmark.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("Gateway Item Controller: get Items By Owner ID {}.", ownerId);
        return itemClient.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> getItemsByText(
            @RequestParam(name = "text") String text,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        log.info("Gateway Item Controller: get Items By Text: {}.", text);
        return itemClient.getItemsByText(text, from, size);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Gateway Item Controller: get Item. User ID {}, item ID {}.", userId, itemId);
        return itemClient.getItem(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class)
            @RequestBody ItemDtoRequest dtoRequest) {
        log.info("Gateway Item Controller: create Item. User ID {}.", userId);
        return itemClient.createItem(userId, dtoRequest);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Create.class)
            @RequestBody CommentDto commentDto) {
        log.info("Gateway Item Controller: create Comment. User ID {}, itemId {}.", userId, itemId);
        return itemClient.createComment(itemId, userId, commentDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Update.class)
            @RequestBody ItemDtoRequest dtoRequest) {
        log.info("Gateway Item Controller: update Item. User ID {}, itemId {}.", userId, itemId);
        return itemClient.updateItem(dtoRequest, itemId, userId);
    }

    @DeleteMapping(value = "/{itemId}")
    public ResponseEntity<Object> deleteItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Gateway Item Controller: delete Item. User ID {}, itemId {}.", userId, itemId);
        return itemClient.deleteItem(itemId, userId);
    }
}