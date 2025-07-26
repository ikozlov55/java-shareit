package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto getItemRequestById(long requestId);

    ItemRequestDto createItemRequest(long userId, ItemRequestDto request);

    List<ItemRequestDto> getUserItemRequests(long userId);

    List<ItemRequestDto> getAllItemRequests(long userId);
}
