package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.RequestItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestItemRepository extends JpaRepository<RequestItem, Long> {

    List<RequestItem> findAllByRequestorIdOrderByCreatedAsc(Long userId);

    @Query(value = "SELECT r " +
            "FROM RequestItem r " +
            "WHERE r.requestor.id <> :userId")
    List<RequestItem> findAllByRequestorIdNotLike(Long userId, Pageable pageable);

    @Query(value = "SELECT r.id FROM requests r " +
            "WHERE r.id = ?1",
            nativeQuery = true)
    Optional<Long> checkIdValue(Long requestId);
}