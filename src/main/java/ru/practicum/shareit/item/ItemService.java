package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto getItemById(long itemId);

    Collection<ItemDto> getUserItems(long userId);

    Collection<ItemDto> itemsSearch(long userId, String text);

    ItemDto createItem(long userId, ItemDto item);

    ItemDto updateItem(long userId, long itemId, ItemDto item);
}
