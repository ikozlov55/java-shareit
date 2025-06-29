package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto getItemById(long itemId) {
        Item itemModel = itemRepository.getItemById(itemId);
        return itemMapper.toDto(itemModel);
    }

    @Override
    public Collection<ItemDto> getUserItems(long userId) {
        return itemRepository.getUserItems(userId).stream().map(itemMapper::toDto).toList();
    }

    @Override
    public Collection<ItemDto> itemsSearch(long userId, String text) {
        userRepository.checkUserExists(userId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.itemsSearch(userId, text).stream().map(itemMapper::toDto).toList();
    }

    @Override
    public ItemDto createItem(long userId, ItemDto item) {
        userRepository.checkUserExists(userId);
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Item name is required!");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Item description is required!");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Item availability is required!");
        }
        Item itemModel = itemMapper.toModel(userId, item);
        return itemMapper.toDto(itemRepository.createItem(itemModel));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto item) {
        userRepository.checkUserExists(userId);
        Item itemModel = itemMapper.toModel(userId, item);
        return itemMapper.toDto(itemRepository.updateItem(itemId, itemModel));
    }
}
