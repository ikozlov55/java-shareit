package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    void checkItemExists(long itemId);

    Item getItemById(long itemId);

    Collection<Item> getUserItems(long userId);

    Collection<Item> itemsSearch(long userId, String text);

    Item createItem(Item item);

    Item updateItem(long itemId, Item item);
}
