package ru.practicum.shareit.request.repository;

import java.util.List;

import ru.practicum.shareit.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequestorIdOrderByCreateAsc(Long userId);

    @Query(value = "SELECT r " +
    "FROM Request r " +
    "WHERE r.requestor_id <> :userId")
    List<Request> findAllByRequestorIdNotLike(Long userId, Pageable pageable);
}