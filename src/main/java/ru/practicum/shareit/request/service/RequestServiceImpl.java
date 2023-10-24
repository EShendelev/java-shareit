package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> findAll(Long userId, int pageNumber, int pageSize) {
        checkUser(userId);
        PageRequest pageRequest = createPageRequest(from, size);

        List<Request> requests = requestRepository.findAllByRequestorIdNotLike(userId, pageRequest);
        log.info("RequestService: поиск всех запросов пользователя ID {}", userId);

        return getRequestDtos(requests);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> findAllByUserId(Long userId) {
        checkUser(userId);

        List<Request> requests = requestRepository.findAllByRequestorIdOrderByCreateAsc(userId);
        log.info("RequestService: findAllByUserId ID {}", userId);
        return getRequestDtos(requests);
    }




    private void checkItem(Long id) {
        itemRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Предмет ID %d не найдено", id))
        );
    }

    private void checkUser(Long id) {
        userRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь ID %d не найдено", id))
        );
    }

    private PageRequest createPageRequest(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber/pageSize, pageSize);
    }

    private List<RequestDto> getRequestDtos(List<Request> requests) {
        Map<Request, List<Item>> itemAll = itemRepository.findAllByRequestIn(requests)
        .stream()
        .collect(Collections.groupingBy(Item::getRequest, Collections.toList()));

        return requests.stream()
        .map(request -> {
            RequestDto requestDto = RequestMapper.toDto(request);
            List<Item> itemList = itemAll.getOrDefault(request, Collections.emptyList());
            requestDto.setItems(itemList.stream().map(ItemMapper::toDtoResponse)
            .collect(Collections.toList()));
            return requestDto;
        }).collect(Collections.toList());
    }

}