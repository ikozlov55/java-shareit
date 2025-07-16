package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getUserItems(@RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> itemsSearch(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                           @RequestParam String text) {
        return itemService.itemsSearch(userId, text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                              @Valid @RequestBody ItemDto item) {
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @Valid @RequestBody ItemDto item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                 @PathVariable long itemId,
                                 @Valid @RequestBody CommentCreateDto comment) {
        return itemService.addComment(userId, itemId, comment);
    }
}
