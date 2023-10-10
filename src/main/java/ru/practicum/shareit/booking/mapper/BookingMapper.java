package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.awt.print.Book;

public class BookingMapper {
    public static BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setItem(new BookingResponseDto.Item(
                booking.getItem().getId(),
                booking.getItem().getName()
        ));
        bookingResponseDto.setBooker(new BookingResponseDto.Booker(
                booking.getBooker().getId(),
                booking.getBooker().getName()
        ));
        bookingResponseDto.setStatus(booking.getStatus());
        return bookingResponseDto;
    }

    public static Booking toModel(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setStatus(Status.WAITING);
    }
}
