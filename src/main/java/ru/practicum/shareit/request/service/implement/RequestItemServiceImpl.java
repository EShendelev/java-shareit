package ru.practicum.shareit.request.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.mapper.RequestItemMapper;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.request.service.interfaces.RequestItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestItemServiceImpl implements RequestItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestItemRepository requestItemRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RequestItemDto> findAll(Long userId, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidateException("параметр from не может быть меньше 0 и size меньше или равен 0 ");
        }
        checkUser(userId);
        PageRequest pageRequest = createPageRequest(from, size);

        List<RequestItem> requestItems = requestItemRepository.findAllByRequestorIdNotLike(userId, pageRequest);
        log.info("RequestService: поиск всех запросов пользователя ID {}", userId);

        return getRequestDtos(requestItems);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestItemDto> findAllByUserId(Long userId) {
        checkUser(userId);
        List<RequestItem> requestItems = requestItemRepository.findAllByRequestorIdOrderByCreatedAsc(userId);
        log.info("RequestService: findAllByUserId ID {}", userId);
        return getRequestDtos(requestItems);
    }

    @Transactional(readOnly = true)
    @Override
    public RequestItemDto findById(Long userId, Long requestId) {
        checkUser(userId);
        RequestItem requestItem = requestItemRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос ID %d не найден", requestId)));

        RequestItemDto requestItemDto = RequestItemMapper.toDto(requestItem);
        requestItemDto.setItems(itemRepository.findAllByRequestId(requestItem.getId()).stream()
                .map(ItemMapper::toDtoResponse)
                .collect(Collectors.toList()));
        log.info("RequestItemService: поиск запроса по ID. Запрос ID {}, пользователь ID {}", requestId, userId);
        return requestItemDto;

    }

    @Transactional
    @Override
    public RequestItemDto save(Long userId, RequestItemDto requestItemDto) {
        User user = checkAndReturnUser(userId);

        RequestItem requestItem = RequestItemMapper.toModel(requestItemDto);
        requestItem.setCreated(LocalDateTime.now());
        requestItem.setRequestor(user);
        requestItemRepository.save(requestItem);
        log.info("RequestItemService: save. User ID {}, request ID {}", userId, requestItem.getId());

        return RequestItemMapper.toDto(requestItem);
    }


    private void checkItem(Long id) {
        itemRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Предмет ID %d не найден", id))
        );
    }

    private User checkAndReturnUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь ID %d не найден", id))
        );
    }

    private void checkUser(Long id) {
        userRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь ID %d не найден", id))
        );
    }

    private PageRequest createPageRequest(int from, int size) {
        return PageRequest.of(from / size, size);
    }

    private List<RequestItemDto> getRequestDtos(List<RequestItem> requestItems) {
        Map<RequestItem, List<Item>> itemAll = itemRepository.findAllByRequestIn(requestItems)
                .stream()
                .collect(Collectors.groupingBy(Item::getRequest, Collectors.toList()));

        return requestItems.stream()
                .map(requestItem -> {
                    RequestItemDto requestItemDto = RequestItemMapper.toDto(requestItem);
                    List<Item> itemList = itemAll.getOrDefault(requestItem, Collections.emptyList());
                    requestItemDto.setItems(itemList.stream().map(ItemMapper::toDtoResponse)
                            .collect(Collectors.toList()));
                    return requestItemDto;
                }).collect(Collectors.toList());
    }

}