package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN User AS us on b.booker.id = us.id " +
            "WHERE us.id = :userId " +
            "AND :time BETWEEN b.start AND b.end")
    List<Booking> findByBookerIdAndCurrent(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, Status status, Pageable pageable);

    List<Booking> findByBookerIdOrderByStartDesc(Long userId, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long id, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN Item AS i ON b.item.id = i.id " +
            "LEFT JOIN User AS us ON i.owner.id = us.id " +
            "WHERE us.id = :userId " +
            "AND :time > b.start AND :time < b.end ")
    List<Booking> findByItemOwnerIdAndCurrentOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);


    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long id, LocalDateTime time, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long id, Status status, Pageable pageable);

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime time);

    @Query(value = "SELECT COUNT(*) FROM bookings " +
            "WHERE booker_id = ?1 AND item_id = ?2 AND end_date < ?3 AND status = 'APPROVED'",
            nativeQuery = true)
    int countApprovedBookingsByUserIdAndItemId(Long userId, Long itemId, LocalDateTime time);

    @Query(value = "SELECT * FROM bookings b " +
            "JOIN public.items i on i.id = b.item_id " +
            "WHERE i.id = ?3 AND i.owner_id = ?2 AND b.status = 'APPROVED' " +
            "AND (b.end_date < ?1 OR ?1 BETWEEN b.start_date AND b.end_date) " +
            "ORDER BY b.start_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Booking findLastBooking(LocalDateTime now, Long ownerId, Long itemId);

    @Query(value = "SELECT * FROM bookings b " +
            "JOIN public.items i on i.id = b.item_id " +
            "WHERE i.id = ?3 AND i.owner_id = ?2 AND b.status = 'APPROVED' " +
            "AND b.start_date > ?1 " +
            "ORDER BY b.start_date " +
            "LIMIT 1",
            nativeQuery = true)
    Booking findNextBooking(LocalDateTime now, Long ownerId, Long itemId);

    List<Booking> findAllByItemInAndStatus(List<Item> items, Status status, Sort sort);

    List<Booking> findAllByItemIdInAndStatus(List<Long> itemIds, Status status, Sort sort);

    @Query(value = "SELECT b.id FROM bookings b " +
            "WHERE b.id = ?1",
            nativeQuery = true)
    Optional<Long> checkIdValue(Long itemId);

    @Query(value = "SELECT count(*) FROM bookings b " +
            "LEFT JOIN public.items i on i.id = b.item_id " +
            "WHERE i.owner_id = ?1",
    nativeQuery = true)
    int findBookinsCountByOwnerId(Long ownerId);
}
