package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}