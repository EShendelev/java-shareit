package ru.practicum.shareit.user.service.implement;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.Collection;

public class UserServiceImpl implements UserService {

    private UserStorage userStorage;

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(long id) {
        return userStorage.get(id);
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);
    }
}
