package ru.practicum.shareit.item.dto;


import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {
    @Getter
    private static final ItemMapper instance = new ItemMapper();

    private ItemMapper() {
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toModel(long ownerId, ItemDto item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }
}
