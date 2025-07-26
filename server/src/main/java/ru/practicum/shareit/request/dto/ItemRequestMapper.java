package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

public final class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequestDto toDto(ItemRequest request) {
        return new ItemRequestDto(request.getId(), request.getDescription(), request.getCreated(), null);
    }

    public static ItemRequest toModel(ItemRequestDto request) {
        return new ItemRequest(null, request.getDescription(), LocalDateTime.now(), null, null);
    }
}
