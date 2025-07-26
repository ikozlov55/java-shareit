package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(@PathVariable long requestId) {
        return itemRequestClient.getItemRequestById(requestId);
    }

    @PostMapping
    ResponseEntity<Object> createItemRequest(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                             @Valid @RequestBody ItemRequestDto request) {
        return itemRequestClient.createItemRequest(userId, request);
    }

    @GetMapping
    ResponseEntity<Object> getUserItemRequests(@RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemRequestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAllItemRequests(@RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemRequestClient.getAllItemRequests(userId);
    }
}
