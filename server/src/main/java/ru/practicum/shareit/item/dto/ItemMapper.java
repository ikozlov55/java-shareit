package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Item;

import java.util.List;

public final class ItemMapper {
    private ItemMapper() {
    }

    public static ItemDto toDto(Item item) {
        Long requestId = item.getItemRequest() != null ? item.getItemRequest().getId() : null;
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), requestId,
                null, null, List.of());
    }


    public static Item toModel(ItemDto item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                null, null);
    }
}
