package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.interfaces.CommentService;
import ru.practicum.shareit.item.service.interfaces.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemResponseDto save(
            @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader(REQUEST_HEADER) Long ownerId) {
        ItemResponseDto itemResponseDto = itemService.save(itemRequestDto, ownerId);
        log.info("Server Item Controller: create Item. User ID {}.", ownerId);
        return itemResponseDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(
            @RequestHeader(REQUEST_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDto commentDto) {
        CommentDto comment = commentService.save(userId, itemId, commentDto);
        log.info("Server Item Controller: create Comment. User ID {}, itemId {}.", userId, itemId);
        return comment;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(
            @RequestBody ItemRequestDto itemRequestDto,
            @PathVariable Long itemId,
            @RequestHeader(REQUEST_HEADER) long ownerId) {
        ItemResponseDto itemResponseDto = itemService.update(ownerId, itemId, itemRequestDto);
        log.info("Server Item Controller: update Item. User ID {}, itemId {}.", ownerId, itemId);
        return itemResponseDto;
    }

    @GetMapping
    public Collection<ItemResponseDto> getAllUserItems(
            @RequestHeader(REQUEST_HEADER) Long ownerId,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Collection<ItemResponseDto> itemsList = itemService.findAllByOwnerId(ownerId, from, size);
        log.info("Server Item Controller: get Items By Owner ID {}.", ownerId);
        return itemsList;
    }

    @GetMapping("/{id}")
    public ItemResponseDto findById(@PathVariable("id") Long itemId,
                                    @RequestHeader(REQUEST_HEADER) Long userId) {
        ItemResponseDto itemResponseDto = itemService.findById(userId, itemId);
        log.info("Server Item Controller: get Item. User ID {}, item ID {}.", userId, itemId);
        return itemResponseDto;
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchByTextRequest(
            @RequestParam String text,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Collection<ItemResponseDto> itemsList = itemService.findItemByText(text, from, size);
        log.info("Server Item Controller: get Items By Text: {}.", text);
        return itemsList;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(REQUEST_HEADER) Long userId,
                       @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
        log.info("Server Item Controller: delete Item. User ID {}, itemId {}.", userId, itemId);
    }

}
