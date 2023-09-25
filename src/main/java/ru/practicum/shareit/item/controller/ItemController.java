package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.validmark.Create;
import ru.practicum.shareit.validmark.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@Validated({Create.class})
                          @RequestBody ItemDto itemDto,
                          @Min(1)
                          @NotNull
                          @RequestHeader(REQUEST_HEADER) long ownerId) {
        Item item = itemService.create(ItemMapper.toModel(itemDto, ownerId), ownerId);
        log.info("Создана запись о предмете ID {}", item.getId());
        return ItemMapper.toDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@Validated({Update.class})
                          @RequestBody ItemDto itemDto,
                          @NotNull
                          @Min(1)
                          @PathVariable("id") long id,
                          @Min(1)
                          @NotNull
                          @RequestHeader(REQUEST_HEADER) long ownerId) {
        Item item = itemService.update(ItemMapper.toModelForUpdate(itemDto, id, ownerId));
        log.info("Обновлена информация по предмету ID {}", id);
        return ItemMapper.toDto(item);
    }

    @GetMapping
    public Collection<ItemDto> getAllUsersItem(@Min(1)
                                               @NotNull
                                               @RequestHeader(REQUEST_HEADER) Long ownerId) {
        Collection<Item> items = itemService.getAllUserItems(ownerId);
        log.info("Получен список всех предметов");
        return ItemMapper.toDtoCollection(items);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") long id) {
        Item item = itemService.get(id);
        log.info("Получена информация по предмету ID {}", id);
        return ItemMapper.toDto(item);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByRequest(@RequestParam String text) {
        Collection<Item> items = itemService.getItemByNameOrDescription(text);
        log.info("Получен список предметов подходящих под запрос \"{}\"", text);
        return ItemMapper.toDtoCollection(items);
    }

}
