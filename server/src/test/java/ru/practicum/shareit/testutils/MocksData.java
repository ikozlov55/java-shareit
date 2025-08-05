package ru.practicum.shareit.testutils;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public final class MocksData {
    private MocksData() {
    }

    public static final ItemDto ITEM = new ItemDto(1L,
            "Самокат",
            "Шикарный самокат, быстрый!",
            true,
            null,
            null,
            null,
            null);

    public static final UserDto USER = new UserDto(1L,
            "Владимир Владимиров",
            "vladimit@mail.ru");

    public static final BookingDto BOOKING = new BookingDto(1L,
            ITEM,
            USER,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(2),
            BookingStatus.APPROVED);

    public static final CommentDto COMMENT = new CommentDto(1L,
            "Отлично!",
            "Владимир",
            LocalDateTime.now().minusDays(1));

    public static final ItemRequestDto ITEM_REQUEST = new ItemRequestDto(1L,
            "Требуется самокат!",
            LocalDateTime.now().minusDays(7),
            List.of(ITEM));
}
