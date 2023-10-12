package ru.practicum.shareit.item.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.CommentRepository;
import ru.practicum.shareit.item.repository.db.ItemRepository;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public ItemResponseDto save(ItemRequestDto itemRequestDto, long ownerId) {
        User user = checkAndReturnUser(ownerId);
        Item item = ItemMapper.toModel(itemRequestDto);
        item.setOwner(user);
        Item saveItem = itemRepository.save(item);
        log.debug("ItemService: сохранение предмета ID {} пользователя ID {}", item.getId(), ownerId);
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

        if (!itemRequestDto.getName().isBlank()) {
            updetableItem.setName(itemRequestDto.getName());
        }

        if (!itemRequestDto.getDescription().isBlank()) {
            updetableItem.setDescription(itemRequestDto.getDescription());
        }

        if (itemRequestDto.getAvailable() != null) {
            if (!Objects.equals(itemRequestDto.getAvailable(), updetableItem.getAvailable())) {
                updetableItem.setAvailable(itemRequestDto.getAvailable());
            }
        }
        log.debug("ItemService: обновлена иформация по предмету ID {} пользователя ID {}", itemId, userId);
        return ItemMapper.toDtoResponse(updetableItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto findById(Long userId, Long itemId) {
        checkAndReturnUser(userId);
        Item item = checkAndReturnItem(itemId);

        ItemResponseDto itemResponseDto = ItemMapper.toDtoResponse(item);
        itemResponseDto.setComments(commentRepository.findAllByItem(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList())
        );

        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);

        itemResponseDto.setLastBoking(lastBooking == null ? null : new ItemResponseDto.ItemBooking(
                lastBooking.getId(),
                lastBooking.getBooker().getId()));
        itemResponseDto.setNextBoking(nextBooking == null ? null : new ItemResponseDto.ItemBooking(
                nextBooking.getId(),
                nextBooking.getBooker().getId()));

        log.debug("ItemService: поиск предмета по ID. Пользователь ID {}, предмет ID {}", userId, itemId);
        return itemResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemResponseDto> findAllByOwnerId(Long ownerId) {
        checkAndReturnUser(ownerId);
        return itemStorage.getAllUserItems(ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemResponseDto> findItemByText(String text) {
        log.debug("ItemService: поиск по совпадениям. Запрос: {}", text);
        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::toDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long itemId) {
        checkAndReturnItem(itemId);
        checkAndReturnUser(userId);
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
}
