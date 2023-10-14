package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.CommentService;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.validmark.Create;
import ru.practicum.shareit.validmark.Update;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

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
    public ItemResponseDto save(@Validated({Create.class})
                                 @RequestBody ItemRequestDto itemRequestDto,
                                 @Min(1)
                                 @NotNull
                                 @RequestHeader(REQUEST_HEADER) Long ownerId) {
        ItemResponseDto itemResponseDto = itemService.save(itemRequestDto, ownerId);
        log.info("Создана запись о предмете ID {}", item.getId());
        return itemResponseDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader(REQUEST_HEADER) Long userId,
                                  @PathVariable Long itemId,
                                  @Valid @RequestBody CommentDto commentDto) {
        CommentDto comment = commentService.save(userId, itemId, commentDto);
        log.info("Создан комментарий к предмету ID {} пользователем ID {}", itemId, userId);
        return comment;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@Validated({Update.class})
                                 @RequestBody ItemRequestDto itemRequestDto,
                                 @PathVariable Long itemId,
                                 @RequestHeader(REQUEST_HEADER) long ownerId) {
        ItemResponseDto itemResponseDto = itemService.update(ownerId, itemId, itemRequestDto);
        log.info("Обновлена информация по предмету ID {} владелец ID {}", itemId, ownerId);
        return itemResponseDto;
    }

    @GetMapping
    public Collection<ItemResponseDto> getAllUserItems(@Min(1)
                                                       @NotNull
                                                       @RequestHeader(REQUEST_HEADER) Long ownerId) {
        Collection<ItemResponseDto> itemsList = itemService.findAllByOwnerId(ownerId);
        log.info("Получен список всех предметов");
        return itemsList;
    }

    @GetMapping("/{id}")
    public ItemResponseDto findById(@PathVariable("id") Long itemId,
                                    @RequestHeader(REQUEST_HEADER) Long userId) {
        ItemResponseDto itemResponseDto = itemService.findById(userId, itemId);
        log.info("Получена информация по предмету ID {}, пользователя ID {}", itemId, userId);
        return itemResponseDto;
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchByTextRequest(@RequestParam String text) {
        Collection<ItemResponseDto> itemsList = itemService.findItemByText(text);
        log.info("Получен список предметов подходящих под запрос \"{}\"", text);
        return itemsList;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(REQUEST_HEADER) Long userId,
                       @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
        log.info("Удаление предмета ID {} пользователя ID {}", itemId, userId);
    }

}
