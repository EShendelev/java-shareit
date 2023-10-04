package ru.practicum.shareit.user.service.interfaces;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAll();

    User get(Long id);

    User save(User user);

    User update(User user);

    void delete(Long id);
}
