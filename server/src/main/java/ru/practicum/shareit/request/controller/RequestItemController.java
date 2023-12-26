package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.service.interfaces.RequestItemService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class RequestItemController {
    private final RequestItemService requestItemService;


    @GetMapping
    public Collection<RequestItemDto> findAllByUserId(@RequestHeader("X-Sharer-User-id") Long userId) {
        log.info("Server item request: get Item Requests By User ID {}.", userId);
        return requestItemService.findAllByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<RequestItemDto> findAll(
            @RequestHeader("X-Sharer-User-id") Long userId,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log.info("Server item request: get All Item Requests User ID {}.", userId);
        return requestItemService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestItemDto findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long requestId) {
        log.info("Server item request: get Item Request User ID {}, request ID {}.",
                userId, requestId);
        return requestItemService.findById(userId, requestId);
    }

    @PostMapping
    public RequestItemDto save(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody RequestItemDto requestItemDto) {
        log.info("Server item request: create Item Request User ID {}.", userId);
        return requestItemService.save(userId, requestItemDto);
    }

}