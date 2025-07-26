package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    ItemRequestDto getItemRequestById(@PathVariable long requestId) {
        return itemRequestService.getItemRequestById(requestId);
    }

    @PostMapping
    ItemRequestDto createItemRequest(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                     @Valid @RequestBody ItemRequestDto request) {
        return itemRequestService.createItemRequest(userId, request);
    }

    @GetMapping
    List<ItemRequestDto> getUserItemRequests(@RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemRequestService.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAllItemRequests(@RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return itemRequestService.getAllItemRequests(userId);
    }


}
