package ru.practicum.shareit.user.repository.in_memory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailConflictException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.repository.in_memory.util.UserIdProvider;

import java.util.*;

@Repository
public class UserDao implements UserStorage {
    UserIdProvider idProvider = new UserIdProvider();
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        Long id = idProvider.getIncrementId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        long id = user.getId();
        checkUser(id);
        User old = users.get(id);
        if (!old.getEmail().equals(user.getEmail())) {
            checkEmail(user.getEmail());
        }
        return user;
    }

    @Override
    public User get(long id) {
        checkUser(id);
        return users.get(id);
    }

    @Override
    public void delete(long id) {
        checkUser(id);
        users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void checkUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с ID %d не найден", id));
        }
    }

    private void checkEmail(String email) {
        if (emails.contains(email) || email == null) {
            throw new EmailConflictException(String.format("Пользователь с %s уже существует", email));
        }
    }
}
