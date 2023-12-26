package ru.practicum.shareit.item.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RequestItemRepository requestItemRepository;


    @Override
    @Transactional
    public ItemResponseDto save(ItemRequestDto itemRequestDto, long ownerId) {
        User user = checkAndReturnUser(ownerId);
        RequestItem requestItem =
                itemRequestDto.getRequestId() == null ? null : checkAndReturnRequestItem(itemRequestDto.getRequestId());
        Item item = ItemMapper.toModel(itemRequestDto);
        item.setOwner(user);
        item.setRequest(requestItem);
        Item saveItem = itemRepository.save(item);
        log.info("ItemService: сохранение предмета ID {} пользователя ID {}", item.getId(), ownerId);
        return ItemMapper.toDtoResponse(saveItem);
    }

    @Override
    @Transactional
    public ItemResponseDto update(Long userId, Long itemId, ItemRequestDto itemRequestDto) {
        Item updetableItem = checkAndReturnItem(itemId);

        if (!updetableItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь ID %d не является владельцем " +
                    "вещи ID %d", userId, itemId));
        }

        if (itemRequestDto.getName() != null && !itemRequestDto.getName().isBlank()) {
            updetableItem.setName(itemRequestDto.getName());
        }

        if (itemRequestDto.getDescription() != null && !itemRequestDto.getDescription().isBlank()) {
            updetableItem.setDescription(itemRequestDto.getDescription());
        }

        if (itemRequestDto.getAvailable() != null) {
            if (!Objects.equals(itemRequestDto.getAvailable(), updetableItem.getAvailable())) {
                updetableItem.setAvailable(itemRequestDto.getAvailable());
            }
        }
        itemRepository.save(updetableItem);
        log.info("ItemService: обновлена иформация по предмету ID {} пользователя ID {}", itemId, userId);
        return ItemMapper.toDtoResponse(updetableItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto findById(Long userId, Long itemId) {
        checkUser(userId);
        Item item = checkAndReturnItem(itemId);

        ItemResponseDto itemResponseDto = ItemMapper.toDtoResponse(item);
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        itemResponseDto.setComments(comments
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList())
        );


        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);

        itemResponseDto.setLastBooking(lastBooking == null ? null : new ItemResponseDto.ItemBooking(
                lastBooking.getId(),
                lastBooking.getBooker().getId()));
        itemResponseDto.setNextBooking(nextBooking == null ? null : new ItemResponseDto.ItemBooking(
                nextBooking.getId(),
                nextBooking.getBooker().getId()));

        log.info("ItemService: поиск предмета по ID. Пользователь ID {}, предмет ID {}", userId, itemId);
        return itemResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemResponseDto> findAllByOwnerId(Long ownerId, int from, int size) {
        checkUser(ownerId);
        List<Long> itemIds = itemRepository.getItemsId(ownerId);
        List<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId, PageRequest.of(from, size));
        Map<Item, List<Comment>> itemsWithComments = commentRepository.findAllByItemIdIn(
                        itemIds, Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .collect(Collectors.groupingBy(Comment::getItem, Collectors.toList()));
        Map<Item, List<Booking>> itemsWithBookings = bookingRepository.findAllByItemIdInAndStatus(
                        itemIds, Status.APPROVED, Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));

        List<ItemResponseDto> itemsResponseDtos = items
                .stream()
                .map(item -> {
                    ItemResponseDto itemResponseDto = ItemMapper.toDtoResponse(item);
                    List<Comment> comments = itemsWithComments.getOrDefault(item, Collections.emptyList());
                    List<Booking> bookings = itemsWithBookings.getOrDefault(item, Collections.emptyList());
                    LocalDateTime now = LocalDateTime.now();

                    Booking lastBooking = bookings.stream()
                            .filter(booking -> ((booking.getEnd().isEqual(now) || booking.getEnd().isBefore(now))
                                    || (booking.getStart().isEqual(now) || booking.getStart().isBefore(now))))
                            .findFirst()
                            .orElse(null);

                    Booking nextBooking = bookings.stream()
                            .filter(booking -> booking.getStart().isAfter(now))
                            .reduce((first, second) -> second)
                            .orElse(null);

                    itemResponseDto.setComments(comments.stream()
                            .map(CommentMapper::toDto)
                            .collect(Collectors.toList()));

                    itemResponseDto.setLastBooking(lastBooking == null ? null : new ItemResponseDto.ItemBooking(
                            lastBooking.getId(),
                            lastBooking.getBooker().getId()));

                    itemResponseDto.setNextBooking(nextBooking == null ? null : new ItemResponseDto.ItemBooking(
                            nextBooking.getId(),
                            nextBooking.getBooker().getId()));

                    return itemResponseDto;
                })
                .collect(Collectors.toList());
        log.info("ItemService: Поиск всех предметов по владельцу. Пользователь ID {}", ownerId);
        return itemsResponseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemResponseDto> findItemByText(String text, int from, int size) {
        if (text.isEmpty()) {
            log.info("Пустой поисковый запрос");
            return new ArrayList<>();
        }
        log.info("ItemService: поиск по совпадениям. Запрос: {}", text);
        return itemRepository.search(text, PageRequest.of(from, size))
                .stream()
                .map(ItemMapper::toDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long itemId) {
        checkItem(itemId);
        checkUser(userId);
        itemRepository.deleteById(itemId);
        log.debug("ItemService: delete");
    }

    private User checkAndReturnUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь  ID %d не найден", id))
        );
    }

    private Item checkAndReturnItem(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Предмет ID %d не найден", id))
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

    private RequestItem checkAndReturnRequestItem(Long id) {
        return requestItemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Запрос ID %d не найден", id))
        );
    }

    private void checkRequestItem(Long id) {
        requestItemRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Запрос ID %d не найден", id))
        );
    }
}
