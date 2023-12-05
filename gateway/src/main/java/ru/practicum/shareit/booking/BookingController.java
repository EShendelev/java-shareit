package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.validmark.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookingsByStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Status status = Status.checkValidStatus(stateParam);
        Status.checkValidStatus(stateParam);
        log.info("Gateway Booking Controller: Get booking with state {}, userId={}, from={}, size={}",
                stateParam, userId, from, size);
        return bookingClient.getBookingsByStatus(userId, status, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getBookingsByOwnerAndStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Status status = Status.checkValidStatus(stateParam);
        log.info("Gateway Booking Controller: Get booking with state {}, userId={}, from={}, size={}",
                stateParam, userId, from, size);
        return bookingClient.getBookingsByOwnerAndStatus(userId, status, from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("Gateway Booking Controller: get booking ID {}, user ID {}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody @Validated(Create.class) BookItemRequestDto requestDto) {
        log.info("Gateway Booking Controller: Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("Gateway Booking Controller: update booking {}, userId={}", bookingId, userId);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @DeleteMapping(value = "/{bookingId}")
    public void deleteBooking(@PathVariable Long bookingId) {
        log.info("Gateway Booking Controller: delete booking {}", bookingId);
        bookingClient.deleteBooking(bookingId);
    }



}