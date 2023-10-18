package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN User AS us on b.booker.id = us.id " +
            "WHERE us.id = :userId " +
            "AND :time BETWEEN b.start AND b.end")
    List<Booking> findByBookerIdAndCurrent(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long userId, Status status, Sort sort);

    List<Booking> findByBookerId(Long userId, Sort sort);

    List<Booking> findByItemOwnerId(Long id, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN Item AS i ON b.item.id = i.id " +
            "LEFT JOIN User AS us ON i.owner.id = us.id " +
            "WHERE us.id = :userId " +
            "AND :time > b.start AND :time < b.end ")
    List<Booking> findByItemOwnerIdAndCurrent(Long userId, LocalDateTime time, Sort sort);


    List<Booking> findByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long id, LocalDateTime time, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Long id, Status status, Sort sort);

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime time);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :ownerId AND b.status = 'APPROVED' " +
            "AND (b.end < :now OR :now BETWEEN b.start AND b.end) " +
            "ORDER BY b.start DESC")
    List<Booking> findLastBooking(LocalDateTime now, Long ownerId, Long itemId);

//    Booking findBookingByItemOwnerIdAndItem_IdAndStartIsAfter(Long ownerId, Long itemId, LocalDateTime now);
//
//    Booking findBookingByItemOwnerIdAndItem_IdAndEndIsBefore(Long ownerId, Long itemId, LocalDateTime now);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :ownerId AND b.status = 'APPROVED' AND b.start > :now " +
            "ORDER BY b.start")
    List<Booking> findNextBooking(LocalDateTime now, Long ownerId, Long itemId);

    List<Booking> findAllByItemInAndStatus(List<Item> items, Status status, Sort sort);
}
