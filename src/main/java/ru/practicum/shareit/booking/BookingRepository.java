package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking>,
        BookingRepositoryExtension {
    @Query("""
            SELECT b
              FROM Booking b
              JOIN b.item i
             WHERE i.id = :itemId
               AND b.status = 'APPROVED'
               AND b.start < :end
               AND b.end > :start
            """)
    Optional<Booking> findConflictingBooking(@Param("itemId") long itemId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
}
