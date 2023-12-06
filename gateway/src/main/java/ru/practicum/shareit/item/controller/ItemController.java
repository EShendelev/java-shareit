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

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class)
            @RequestBody ItemDtoRequest dtoRequest) {
        log.info("Gateway Item Controller: create Item. User ID {}.", userId);
        return itemClient.save(userId, dtoRequest);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> saveComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Create.class)
            @RequestBody CommentDto commentDto) {
        log.info("Gateway Item Controller: create Comment. User ID {}, itemId {}.", userId, itemId);
        return itemClient.saveComment(itemId, userId, commentDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Update.class)
            @RequestBody ItemDtoRequest dtoRequest) {
        log.info("Gateway Item Controller: update Item. User ID {}, itemId {}.", userId, itemId);
        return itemClient.update(dtoRequest, itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("Gateway Item Controller: get Items By Owner ID {}.", ownerId);
        return itemClient.getAllUserItems(ownerId, from, size);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Gateway Item Controller: get Item. User ID {}, item ID {}.", userId, itemId);
        return itemClient.findById(itemId, userId);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchByTextRequest(
            @RequestParam(name = "text") String text,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        log.info("Gateway Item Controller: get Items By Text: {}.", text);
        return itemClient.searchByTextRequest(text, from, size);
    }

    @DeleteMapping(value = "/{itemId}")
    public ResponseEntity<Object> delete(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Gateway Item Controller: delete Item. User ID {}, itemId {}.", userId, itemId);
        return itemClient.delete(itemId, userId);
    }
}