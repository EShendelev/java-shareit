package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    @PostMapping
    public Item create(ItemDto itemDto) {
        return null;
    }

    @PatchMapping("/{id}")
    public Item update(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping
    public Collection<Item> getAll() {
        return null;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping("/search")
    public Collection<Item> searchByRequest(@RequestParam String request) {
        return null;
    }

}
