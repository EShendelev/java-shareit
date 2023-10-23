package ru.practicum.shareit.user.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getAll() {
        log.debug("UserService: Выполняется вывод всех пользователей");
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto get(Long id) {
        checkUser(id);
        UserDto user = UserMapper.toDto(userRepository.getById(id));
        log.debug("UserService: Выполняется вывод пользователя ID {}", id);
        return user;
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        User user = userRepository.save(UserMapper.toModel(userDto, null));
        log.debug("UserService: Выполняется создание пользователя ID {}", user.getId());
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User old = checkAndReturnUser(id);
        userDto.setId(id);
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            userDto.setEmail(old.getEmail());
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            userDto.setName(old.getName());
        }

        log.debug("UserService: обновлена информация пользователя ID {}", id);
        userRepository.save(UserMapper.toModel(userDto, id));
        return userDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkUser(id);
        log.debug("UserService: Удаляется пользователь ID {}", id);
        userRepository.deleteById(id);
    }

    private User checkAndReturnUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь ID %d не найден", id))
        );
    }

    private void checkUser(Long id) {
        userRepository.checkIdValue(id).orElseThrow(
                () -> new NotFoundException(String.format("Бронирование ID %d не найдено", id))
        );
    }
}
