package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepositoryExtension {
    Booking findByIdOrThrow(long bookingId);
}
