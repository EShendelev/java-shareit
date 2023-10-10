package ru.practicum.shareit.booking.service.interfaces;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingService {
    Collection<BookingResponseDto> getAllByState(Long userId, String stateText);

    Collection<BookingResponseDto> getAllByOwnerAndState(Long userId, String stateText);

    BookingResponseDto get(Long userId, Long bookingId);

    BookingResponseDto save(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto updateState(Long userId, Long bookingId, Boolean approved);

    void delete(Long bookingId);

}
