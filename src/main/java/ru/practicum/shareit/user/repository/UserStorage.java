package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {


    User create(User user);

    User update(User user);

    User get(long id);

    void delete(long id);

    Collection<User> getAll();
}
