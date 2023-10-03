package ru.practicum.shareit.user.repository.memory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailConflictException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.repository.memory.util.UserIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@AllArgsConstructor
public class UserDao implements UserStorage {
    UserIdProvider idProvider;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long,String> emails = new HashMap<>();

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        Long id = idProvider.getIncrementId();
        user.setId(id);
        users.put(id, user);
        emails.put(id, user.getEmail());
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
        if (user.getEmail() == null) {
            user.setEmail(old.getEmail());
        }
        if (user.getName() == null) {
            user.setName(old.getName());
        }
        emails.put(id, user.getEmail());
        users.put(id, user);
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
        emails.remove(id);
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
        if (emails.containsValue(email)) {
            throw new EmailConflictException(String.format("Пользователь с %s уже существует", email));
        }
    }
}
