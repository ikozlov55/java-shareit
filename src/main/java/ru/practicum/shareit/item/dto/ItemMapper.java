package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;

public class ItemMapper {
    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }
}
