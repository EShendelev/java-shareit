package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    @PostMapping
    public ItemDto create(ItemDto itemDto) {
        return null;
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping
    public Collection<ItemDto> getAll() {
        return null;
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByRequest(@RequestParam String request) {
        return null;
    }

}
