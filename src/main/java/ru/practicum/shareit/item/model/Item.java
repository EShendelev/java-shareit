package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    @Min(1)
    @NotNull(message = "ID не может быть пустым")
    private long id;
    private String name;
    private String description;
    private boolean available;
    private long ownerId;
    private ItemRequest request;
}
