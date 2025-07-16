package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingsState;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(long userId, BookingCreateDto booking) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start.isEqual(end) || start.isAfter(end)) {
            throw new ValidationException("Invalid booking interval!");
        }
        User user = userRepository.findByIdOrThrow(userId);
        Item item = itemRepository.findByIdOrThrow(booking.getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking!");
        }
        bookingRepository.findConflictingBooking(item.getId(), start, end).ifPresent(i -> {
            throw new ConflictException("Item already booked for that period!");
        });
        Booking bookingModel = new Booking(null, item, user, start, end, BookingStatus.WAITING);
        return BookingMapper.toDto(bookingRepository.save(bookingModel));
    }

    @Override
    public BookingDto approveBooking(long userId, long bookingId, boolean approved) {
        Booking bookingModel = bookingRepository.findByIdOrThrow(bookingId);
        if (bookingModel.getItem().getOwner().getId() != userId) {
            throw new PermissionException("Booking can only be approved by owner!");
        }
        bookingModel.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toDto(bookingRepository.save(bookingModel));
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        userRepository.findByIdOrThrow(userId);
        Booking bookingModel = bookingRepository.findByIdOrThrow(bookingId);
        long bookerId = bookingModel.getBooker().getId();
        long ownerId = bookingModel.getItem().getOwner().getId();
        if (!Set.of(bookerId, ownerId).contains(userId)) {
            throw new PermissionException("Booking info view denied!");
        }
        return BookingMapper.toDto(bookingModel);
    }

    @Override
    public List<BookingDto> getBookings(long userId, BookingsState state) {
        userRepository.findByIdOrThrow(userId);
        Sort sort = Sort.by("start").descending();
        Specification<Booking> spec = Specification
                .where(BookingSpecification.hasBookerId(userId))
                .and(BookingSpecification.hasState(state));
        return bookingRepository.findAll(spec, sort).stream().map(BookingMapper::toDto).toList();
    }

    @Override
    public List<BookingDto> getOwnerBookings(long userId, BookingsState state) {
        userRepository.findByIdOrThrow(userId);
        Specification<Booking> spec = Specification
                .where(BookingSpecification.hasOwnerId(userId))
                .and(BookingSpecification.hasState(state));
        return bookingRepository.findAll(spec).stream().map(BookingMapper::toDto).toList();
    }
}
