package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepositoryExtension {
    Item findByIdOrThrow(long itemId);
}
