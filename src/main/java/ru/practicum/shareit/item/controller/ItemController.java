package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
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
    public ItemRequestDto create(@Validated({Create.class})
                          @RequestBody ItemRequestDto itemRequestDto,
                                 @Min(1)
                          @NotNull
                          @RequestHeader(REQUEST_HEADER) long ownerId) {
        Item item = itemService.save(ItemMapper.toModel(itemRequestDto), ownerId);
        log.info("Создана запись о предмете ID {}", item.getId());
        return ItemMapper.toDto(item);
    }

    @PatchMapping("/{id}")
    public ItemRequestDto update(@Validated({Update.class})
                          @RequestBody ItemRequestDto itemRequestDto,
                                 @NotNull
                          @Min(1)
                          @PathVariable("id") long id,
                                 @Min(1)
                          @NotNull
                          @RequestHeader(REQUEST_HEADER) long ownerId) {
        Item item = itemService.update(ItemMapper.toModelForUpdate(itemRequestDto, id, ownerId));
        log.info("Обновлена информация по предмету ID {}", id);
        return ItemMapper.toDto(item);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUsersItem(@Min(1)
                                               @NotNull
                                               @RequestHeader(REQUEST_HEADER) Long ownerId) {
        Collection<Item> items = itemService.findAllByOwnerId(ownerId);
        log.info("Получен список всех предметов");
        return ItemMapper.toDtoCollection(items);
    }

    @GetMapping("/{id}")
    public ItemRequestDto get(@PathVariable("id") long id) {
        Item item = itemService.findById(id);
        log.info("Получена информация по предмету ID {}", id);
        return ItemMapper.toDto(item);
    }

    @GetMapping("/search")
    public Collection<ItemRequestDto> searchByRequest(@RequestParam String text) {
        Collection<Item> items = itemService.findItemByText(text);
        log.info("Получен список предметов подходящих под запрос \"{}\"", text);
        return ItemMapper.toDtoCollection(items);
    }

}
