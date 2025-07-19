package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingSpecification;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingsState;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findByIdOrThrow(itemId);
        ItemDto itemDto = ItemMapper.toDto(item);
        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getUserItems(long userId) {
        userRepository.findByIdOrThrow(userId);
        return itemRepository.findByOwnerId(userId).stream().map(item -> {
            ItemDto ownerItemDto = ItemMapper.toDto(item);
            Optional<Booking> lastBooking = bookingRepository.findOne(Specification
                    .where(BookingSpecification.hasOwnerId(userId))
                    .and(BookingSpecification.hasState(BookingsState.PAST)));
            Optional<Booking> nextBooking = bookingRepository.findOne(Specification
                    .where(BookingSpecification.hasOwnerId(userId))
                    .and(BookingSpecification.hasState(BookingsState.FUTURE)));
            lastBooking.ifPresent(booking -> ownerItemDto.setLastBooking(BookingMapper.toDto(booking)));
            nextBooking.ifPresent(booking -> ownerItemDto.setNextBooking(BookingMapper.toDto(booking)));
            List<CommentDto> comments = commentRepository.findByItemId(ownerItemDto.getId())
                    .stream()
                    .map(CommentMapper::toDto)
                    .toList();
            ownerItemDto.setComments(comments);
            return ownerItemDto;
        }).toList();
    }

    @Override
    public Collection<ItemDto> itemsSearch(long userId, String text) {
        userRepository.findByIdOrThrow(userId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.itemsSearch(text.toLowerCase()).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public ItemDto createItem(long userId, ItemDto item) {
        User user = userRepository.findByIdOrThrow(userId);
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Item name is required!");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Item description is required!");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Item availability is required!");
        }
        Item itemModel = ItemMapper.toModel(item);
        itemModel.setOwner(user);
        return ItemMapper.toDto(itemRepository.save(itemModel));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto item) {
        userRepository.findByIdOrThrow(userId);
        Item itemModel = itemRepository.findByIdOrThrow(itemId);
        if (!itemModel.getOwner().getId().equals(userId)) {
            throw new PermissionException("Item update request denied!");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            itemModel.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemModel.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemModel.setAvailable(item.getAvailable());
        }
        return ItemMapper.toDto(itemRepository.save(itemModel));
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentCreateDto comment) {
        User user = userRepository.findByIdOrThrow(userId);
        Item item = itemRepository.findByIdOrThrow(itemId);
        Specification<Booking> spec = Specification
                .where(BookingSpecification.hasBookerId(userId))
                .and(BookingSpecification.hasItemId(itemId))
                .and(BookingSpecification.hasState(BookingsState.PAST));
        bookingRepository.findOne(spec).orElseThrow(() -> {
            String reason = String.format("Your booking of item %d not found, or has invalid state!", itemId);
            return new ValidationException(reason);
        });
        commentRepository.findByItemIdAndAuthorId(itemId, userId)
                .ifPresent(c -> {
                    throw new ValidationException("You have already left a comment for this item!");
                });
        Comment newComment = new Comment(null, comment.getText(), item, user, LocalDateTime.now());
        return CommentMapper.toDto(commentRepository.save(newComment));
    }
}
