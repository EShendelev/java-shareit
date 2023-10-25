package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.validmark.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public Collection<BookingResponseDto> findAllByState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive int size) {
      Status.checkValidStatus(stateText);
        log.info("BookingController. GET /bookings. User ID {}, {}", userId, stateText);
        return bookingService.getAllByState(userId, stateText, from, size);
    }

    @GetMapping(value = "/owner")
    public Collection<BookingResponseDto> findAllByOwnerIdAndState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive int size) {
        Status.checkValidStatus(stateText);
        log.info("BookingController. GET /owner. User ID {}, {}", userId, stateText);
        return bookingService.getAllByOwnerIdAndState(userId, stateText, from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("BookingController: GET /{bookingId}. User ID {}, booking ID {}.", userId, bookingId);
        return bookingService.findById(userId, bookingId);
    }

    @PostMapping
    public BookingResponseDto save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class) @RequestBody BookingRequestDto bookingRequestDto) {
        if (!bookingRequestDto.getEnd().isAfter(bookingRequestDto.getStart())) {
            throw new ValidateException(String.format("Ошибка: Время конца бронирования %s перед временем начала %s",
                    bookingRequestDto.getEnd(), bookingRequestDto.getStart()));
        }
        log.info("BookingController: POST /booking. User ID {}.", userId);
        return bookingService.save(userId, bookingRequestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto updateState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("BookingController: PATCH /{bookingId}. User ID {}, booking ID {}.", userId, bookingId);
        return bookingService.updateState(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Long bookingId) {
        log.info("BookingController: DELETE /{bookingId}. Booking ID {}.", bookingId);
        bookingService.delete(bookingId);
    }

}
