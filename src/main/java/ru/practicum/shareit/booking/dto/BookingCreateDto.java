package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class BookingCreateDto {
    @NotNull
    private Long itemId;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
