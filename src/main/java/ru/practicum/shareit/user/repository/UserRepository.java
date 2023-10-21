package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u.id FROM users u " +
            "WHERE u.id = ?1",
            nativeQuery = true)
    Optional<Long> checkIdValue(Long itemId);
}
