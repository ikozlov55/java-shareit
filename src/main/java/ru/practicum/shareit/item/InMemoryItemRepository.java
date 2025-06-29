package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private static Long nextEntityId = 1L;
    private final Map<Long, Item> itemsMap = new HashMap<>();

    @Override
    public void checkItemExists(long itemId) {
        if (itemsMap.containsKey(itemId)) {
            return;
        }
        throw new NotFoundException(String.format("Item %d not found!", itemId));
    }

    @Override
    public Item getItemById(long itemId) {
        checkItemExists(itemId);
        return itemsMap.get(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long userId) {
        return itemsMap.values().stream().filter(x -> x.getOwnerId().equals(userId)).toList();
    }

    @Override
    public Collection<Item> itemsSearch(long userId, String text) {
        final String query = text.toLowerCase();
        return itemsMap.values().stream()
                .filter(x -> {
                    if (!x.getAvailable()) {
                        return false;
                    }
                    return x.getName().toLowerCase().contains(query) ||
                            x.getDescription().toLowerCase().contains(query);
                })
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        item.setId(nextEntityId);
        itemsMap.put(nextEntityId, item);
        nextEntityId++;
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        checkItemExists(itemId);
        Item storedItem = itemsMap.get(itemId);
        if (!storedItem.getOwnerId().equals(item.getOwnerId())) {
            throw new PermissionException("Item update request denied!");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            storedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            storedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            storedItem.setAvailable(item.getAvailable());
        }
        return storedItem;
    }
}
