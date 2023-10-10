package ru.practicum.shareit.user.service.interfaces;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll();

    UserDto get(Long id);

    UserDto save(UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    void delete(Long id);
}
