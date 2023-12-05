package ru.practicum.shareit.booking.service.interfaces;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingService {
    Collection<BookingResponseDto> getAllByState(Long userId, String stateText, int from, int size);

    Collection<BookingResponseDto> getAllByOwnerIdAndState(Long userId, String stateText, int from, int size);

    BookingResponseDto findById(Long userId, Long bookingId);

    BookingResponseDto save(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto updateState(Long userId, Long bookingId, Boolean approved);

    void delete(Long bookingId);

}
