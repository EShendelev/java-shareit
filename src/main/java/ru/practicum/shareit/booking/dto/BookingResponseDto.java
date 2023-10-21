package ru.practicum.shareit.booking.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingItemDto item;
    private Booker booker;
    private Status status;

    @Data
    public static class BookingItemDto {
        private final Long id;
        private final String name;
    }

    @Data
    public static class Booker {
        private final Long id;
        private final String name;
    }

}
