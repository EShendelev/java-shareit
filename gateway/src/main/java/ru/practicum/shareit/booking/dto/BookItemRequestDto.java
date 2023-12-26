package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validmark.Create;
import ru.practicum.shareit.validmark.DateNotNull;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@DateNotNull(groups = {Create.class})
public class BookItemRequestDto {
    private long itemId;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @Future(groups = {Create.class})
    private LocalDateTime end;
}