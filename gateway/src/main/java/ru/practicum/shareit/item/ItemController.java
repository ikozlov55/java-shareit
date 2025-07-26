package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId) {
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> itemsSearch(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                              @RequestParam String text) {
        return itemClient.itemsSearch(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                             @Valid @RequestBody ItemDto item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Item name is required!");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Item description is required!");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Item availability is required!");
        }
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody ItemDto item) {
        return itemClient.updateItem(userId, itemId, item);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentCreateDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }
}
