package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;


public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                ItemMapper.toDto(booking.getItem()),
                UserMapper.toDto(booking.getBooker()),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }
}
