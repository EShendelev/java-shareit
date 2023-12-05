package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.RequestItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i " +
            "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "AND i.available = true")
    List<Item> search(String text, PageRequest pageRequest);

    List<Item> findAllByOwnerIdOrderById(Long userId, PageRequest pageRequest);

    @Query("SELECT i.id FROM Item i")
    List<Long> getItemsId(Long userId);

    @Query(value = "SELECT i.id FROM items i " +
            "WHERE i.id = ?1",
            nativeQuery = true)
    Optional<Long> checkIdValue(Long itemId);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIn(List<RequestItem> requests);
}
