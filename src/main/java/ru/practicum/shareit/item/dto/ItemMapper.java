package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item toModel(long ownerId, ItemDto item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }
}
