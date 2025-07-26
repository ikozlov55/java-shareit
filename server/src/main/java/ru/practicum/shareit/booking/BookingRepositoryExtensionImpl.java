package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

public class BookingRepositoryExtensionImpl implements BookingRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Booking findByIdOrThrow(long bookingId) {
        return entityManager.createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", bookingId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Booking %d not found!", bookingId)));
    }
}
