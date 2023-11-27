package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.service.interfaces.RequestItemService;
import ru.practicum.shareit.validmark.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
        log.info("Вывод всех запросов пользователя ID {}", userId);
        return requestItemService.findAllByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<RequestItemDto> findAll(@RequestHeader("X-Sharer-User-id") Long userId,
                                              @RequestParam(value = "from", defaultValue = "0")
                                              @PositiveOrZero int from,
                                              @RequestParam(value = "size", defaultValue = "10")
                                              @Positive int size) {

        log.info("Вывод всех запросов доступных для пользователя ID {}", userId);
        return requestItemService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestItemDto findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long requestId) {
        log.info("Получение данных о запросе ID {}, пользователь ID {}", requestId, userId);
        return requestItemService.findById(userId, requestId);
    }

    @PostMapping
    public RequestItemDto save(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @Validated({Create.class}) @RequestBody RequestItemDto requestItemDto) {
        log.info("добавление запроса пользователем ID {}", userId);
        return requestItemService.save(userId, requestItemDto);
    }

}