package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingsState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(long userId, BookingCreateDto booking);

    BookingDto approveBooking(long userId, long bookingId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookings(long userId, BookingsState state);

    List<BookingDto> getOwnerBookings(long userId, BookingsState state);
}
