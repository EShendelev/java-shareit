package ru.practicum.shareit.booking.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllByState(Long userId, String stateText, int from, int size) {
        checkUser(userId);
        Pageable pageable = createPageRequest(from, size);

        switch (Status.valueOf(stateText)) {
            case CURRENT:
                log.debug("BookingService: поиск всех бронирований по cостоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository.findByBookerIdAndCurrent(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                log.debug("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
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
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                log.info("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.WAITING,
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            case REJECTED:
                log.info("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}, состояние {}",
                        userId, stateText);
                return bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.REJECTED,
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
            default:
                log.info("BookingService: поиск всех бронирований по состоянию. Пользователь ID {}," +
                        " состояние по умолчанию", userId);
                List<BookingResponseDto> listDto = bookingRepository
                        .findByBookerIdOrderByStartDesc(
                                userId,
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
                return listDto;
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllByOwnerIdAndState(Long userId, String stateText, int from, int size) {
        checkUser(userId);

        Pageable pageable = createPageRequest(from, size);

        int countOfBookings = bookingRepository.findBookinsCountByOwnerId(userId);

        if (countOfBookings == 0) {
            throw new NotFoundException(String.format("У пользователя ID %d нет бронирований", userId));
        }

        switch (Status.valueOf(stateText)) {
            case CURRENT:
                log.debug("BookingService: поиск бронирований по ID владельца и состоянию. ID пользователя {}, " +
                        "состояние {}", userId, stateText);
                return bookingRepository.findByItemOwnerIdAndCurrentOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList()
                        );
            case PAST:
                log.debug("BookingService: поиск бронирований по ID владельца и состоянию. ID пользователя {}, " +
                        "состояние {}", userId, stateText);
                return bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList()
                        );
            case FUTURE:
                log.debug("BookingService: поиск бронирований по ID владельца и состоянию. ID пользователя {}, " +
                        "состояние {}", userId, stateText);
                return bookingRepository.findByItemOwnerIdAndStartIsAfter(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList()
                        );
            case WAITING:
                log.debug("BookingService: поиск бронирований по ID владельца и состоянию. ID пользователя {}, " +
                        "состояние {}", userId, stateText);
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.WAITING,
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList()
                        );
            case REJECTED:
                log.debug("BookingService: поиск бронирований по ID владельца и состоянию. ID пользователя {}, " +
                        "состояние {}", userId, stateText);
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.REJECTED,
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList()
                        );
            default:
                log.debug("BookingService: поиск бронирований по ID владельца и состоянию. ID пользователя {}, " +
                        "состояние {}", userId, stateText);
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(
                                userId,
                                pageable)
                        .stream()
                        .map(BookingMapper::toResponseDto)
                        .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto findById(Long userId, Long bookingId) {
        Booking booking = checkAndReturnBooking(bookingId);

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь ID {} не является владельцем или " +
                    "заказчиком бронирования ID {}", userId, bookingId));
        }
        log.debug("BookingService: полечение бронирования ID {}", bookingId);
        return BookingMapper.toResponseDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto save(Long userId, BookingRequestDto bookingRequestDto) {
        Booking booking = BookingMapper.toModel(bookingRequestDto);
        booking.setBooker(checkAndReturnUser(userId));
        Item item = checkAndReturnItem(bookingRequestDto.getItemId());

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец не может забронировать предмет");
        }

        if (!item.getAvailable()) {
            throw new ValidateException(String.format("Предмет ID %d не доступен", item.getId()));
        }
        booking.setItem(item);
        Booking resultBooking = bookingRepository.save(booking);
        return BookingMapper.toResponseDto(resultBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateState(Long userId, Long bookingId, Boolean approved) {
        Booking booking = checkAndReturnBooking(bookingId);

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Только владелец может изменить статус");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidateException("Бронирование уже подтверждено");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        log.debug("BookingServise: обновление статуса. Пользователь ID {}, бронирование ID {}", userId, bookingId);
        return BookingMapper.toResponseDto(booking);
    }

    @Override
    @Transactional
    public void delete(Long bookingId) {
        checkBooking(bookingId);
        log.debug("BookingService: удаление бронирования");
        bookingRepository.deleteById(bookingId);
    }

    private User checkAndReturnUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь  ID %d не найден", id))
        );
    }

    private void checkItem(Long id) {
        itemRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Предмет ID %d не найден", id))
        );
    }

    private void checkUser(Long id) {
        userRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь ID %d не найден", id))
        );
    }

    private void checkBooking(Long id) {
        bookingRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Бронирование ID %d не найдено", id))
        );
    }

    private Item checkAndReturnItem(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Предмет ID %d не найден", id))
        );
    }

    private Booking checkAndReturnBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Бронирование ID %d не найдено", id))
        );
    }

    private PageRequest createPageRequest(int from, int size) {
        return PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
    }
}
