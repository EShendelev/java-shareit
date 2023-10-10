package ru.practicum.shareit.booking.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.BookingsNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllByState(Long userId, String stateText) {
        checkAndReturnUser(userId);

        switch (Status.valueOf(stateText)) {
            case CURRENT:
                log.debug("BookingService: поиск всех бронирований по cостоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository.findByBookerIdAndCurrent(
                                userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                log.debug("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository.findByBookerIdAndEndIsBefore(
                                userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                log.info("BookingService:  поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository
                        .findByBookerIdAndStartIsAfter(
                                userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                log.info("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository
                        .findByBookerIdAndStatus(
                                userId,
                                Status.WAITING,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case REJECTED:
                log.info("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository
                        .findByBookerIdAndStatus(
                                userId,
                                Status.REJECTED,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            default:
                log.info("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}," +
                        " состояние по умолчанию", userId);
                return bookingRepository
                        .findByBookerId(
                                userId,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllByOwnerAndState(Long userId, String stateText) {
        checkAndReturnUser(userId);
        List<BookingResponseDto> bookings = bookingRepository.findByItemOwnerId(
                        userId,
                        Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .map(BookingMapper::toResponseDto)
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new BookingsNotFoundException(String.format("У пользователя ID %d нет бронирований", userId));
        }
        return null;
    }

    @Override
    public BookingResponseDto get(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public BookingResponseDto save(Long userId, BookingRequestDto bookingRequestDto) {
        return null;
    }

    @Override
    public BookingResponseDto updateState(Long userId, Long bookingId, Boolean approved) {
        return null;
    }

    @Override
    public void delete(Long bookingId) {

    }

    private User checkAndReturnUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь  ID %d не найден", id))
        );
    }

    private Item checkAndReturnItem(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException(String.format("Предмет ID %d не найден", id))
        );
    }
}
