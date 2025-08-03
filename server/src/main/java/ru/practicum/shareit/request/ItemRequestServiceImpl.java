package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto getItemRequestById(long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findByIdOrThrow(requestId);
        ItemRequestDto requestDto = ItemRequestMapper.toDto(itemRequest);
        requestDto.setItems(itemRequest.getItems().stream().map(ItemMapper::toDto).toList());
        return requestDto;
    }

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto request) {
        User user = userRepository.findByIdOrThrow(userId);
        ItemRequest requestModel = ItemRequestMapper.toModel(request);
        requestModel.setUser(user);
        requestModel.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toDto(itemRequestRepository.save(requestModel));
    }

    @Override
    public List<ItemRequestDto> getUserItemRequests(long userId) {
        List<ItemRequest> requests = itemRequestRepository.findByUserId(userId);

        return requests.stream().map(r -> {
            ItemRequestDto requestDto = ItemRequestMapper.toDto(r);
            requestDto.setItems(r.getItems().stream().map(ItemMapper::toDto).toList());
            return requestDto;
        }).toList();
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(long userId) {
        return itemRequestRepository.findByUserIdNot(userId).stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }
}
