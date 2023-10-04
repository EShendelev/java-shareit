package ru.practicum.shareit.user.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepository;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<User> getAll() {
        log.debug("UserService: Выполняется вывод всех пользователей");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User get(Long id) {
        log.debug("UserService: Выполняется вывод пользователя ID {}", id);
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        log.debug("UserService: Выполняется создание пользователя {}", user.getName());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        Long id = user.getId();
        User old = checkAndReturnUser(id);

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(old.getEmail());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(old.getName());
        }
        log.debug("UserService: обновлена информация пользователя ID {}", id);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkAndReturnUser(id);
        log.debug("UserService: Удаляется пользователь ID {}", id);
    }

    private User checkAndReturnUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь ID %d не найден", id))
        );
    }
}
