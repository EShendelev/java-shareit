package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemOwner owner;
    private ItemBooking lastBooking;
    private ItemBooking nextBooking;
    private List<CommentDto> comments;
    private Long requestId;

    @Data
    public static class ItemOwner {
        private final long id;
        private final String name;
    }

    @Data
    public static class ItemBooking {
        private final long id;
        private final long bookerId;
    }
}
