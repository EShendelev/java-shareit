package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;
}
