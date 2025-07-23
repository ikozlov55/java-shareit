package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestRepositoryExtension {
    ItemRequest findByIdOrThrow(long requestId);
}
