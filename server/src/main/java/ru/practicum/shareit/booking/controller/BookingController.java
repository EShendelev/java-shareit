package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.ValidateException;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public Collection<BookingResponseDto> findAllByStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Server Booking Controller: Get booking with state {}, userId={}, from={}, size={}",
                stateText, userId, from, size);
        return bookingService.getAllByState(userId, stateText, from, size);
    }

    @GetMapping(value = "/owner")
    public Collection<BookingResponseDto> findAllByOwnerIdAndStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Server Booking Controller: Get booking with state {}, userId={}, from={}, size={}",
                stateText, userId, from, size);
        return bookingService.getAllByOwnerIdAndStatus(userId, stateText, from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("Server Booking Controller: get booking ID {}, user ID {}", bookingId, userId);
        return bookingService.findById(userId, bookingId);
    }

    @PostMapping
    public BookingResponseDto save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingRequestDto bookingRequestDto) {
        if (!bookingRequestDto.getEnd().isAfter(bookingRequestDto.getStart())) {
            throw new ValidateException(String.format("Ошибка: Время конца бронирования %s перед временем начала %s",
                    bookingRequestDto.getEnd(), bookingRequestDto.getStart()));
        }
        log.info("Server Booking Controller: Creating booking {}, userId={}", bookingRequestDto, userId);
        return bookingService.save(userId, bookingRequestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("Server Booking Controller: update booking {}, userId={}", bookingId, userId);
        return bookingService.updateState(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Long bookingId) {
        log.info("Server Booking Controller: delete booking {}", bookingId);
        bookingService.delete(bookingId);
    }

}
