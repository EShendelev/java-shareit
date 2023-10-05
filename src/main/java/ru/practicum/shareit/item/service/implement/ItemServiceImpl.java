package ru.practicum.shareit.item.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.CommentRepository;
import ru.practicum.shareit.item.repository.db.ItemRepository;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item save(Item item, long ownerId) {
        User user = checkAndReturnUser(ownerId);
        item.setOwner(user);
        Item saveItem = itemRepository.save(item);
        log.debug("ItemService: сохранение предмета ID {} пользователя ID {}", item.getId(), ownerId);
        return saveItem;
    }

    @Override
    @Transactional
    public Item update(Long userId, Long itemId, Item item) {
        Item updetableItem = checkAndReturnItem(itemId);

        if (!updetableItem.getOwner().getId().equals(userId)) {
            throw new UserNotFoundException(String.format("Пользователь ID %d не является владельцем " +
                    "вещи ID %d", userId, itemId));
        }


        /**
         *         long id = forUpdate.getId();
         *         checkIdItem(id);
         *         Item updatable = items.get(id);
         *         checkItemOwner(updatable, forUpdate);
         *
         *         if (forUpdate.getName() != null) {
         *             updatable.setName(forUpdate.getName());
         *         }
         *
         *         if (forUpdate.getDescription() != null) {
         *             updatable.setDescription(forUpdate.getDescription());
         *         }
         *
         *         if (forUpdate.getAvailable() != null) {
         *             if (!Objects.equals(forUpdate.getAvailable(), updatable.getAvailable())) {
         *
         *                 updatable.setAvailable(forUpdate.getAvailable());
         *             }
         *         }
         *         return updatable;
         */
        return itemStorage.update(item);
    }

    @Override
    public Item get(Long id) {
        return itemStorage.get(id);
    }

    @Override
    public Collection<Item> getAllById(Long ownerId) {
        return itemStorage.getAllUserItems(ownerId);
    }

    @Override
    public Collection<Item> getItemByText(String text) {
        return itemStorage.getItemByNameOrDescription(text);
    }

    @Override
    public void delete(Long userId, Long itemId) {
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
