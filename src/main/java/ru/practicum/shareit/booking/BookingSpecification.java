package ru.practicum.shareit.booking;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingsState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class BookingSpecification {

    public static Specification<Booking> started() {
        return (root, query, builder) -> {
            LocalDateTime now = LocalDateTime.now();
            return builder.lessThan(root.get("start"), now);
        };
    }

    public static Specification<Booking> finished() {
        return (root, query, builder) -> {
            LocalDateTime now = LocalDateTime.now();
            return builder.lessThan(root.get("end"), now);
        };
    }

    public static Specification<Booking> hasStatusIn(List<BookingStatus> statuses) {
        return (root, query, builder) -> root.get("status").in(statuses);
    }

    public static Specification<Booking> hasBookerId(long userId) {
        return (root, query, builder) -> {
            Join<Booking, User> user = root.join("booker");
            return builder.equal(user.get("id"), userId);
        };
    }

    public static Specification<Booking> hasOwnerId(long userId) {
        return (root, query, builder) -> {
            Join<Booking, Item> item = root.join("item");
            return builder.equal(item.get("owner").get("id"), userId);
        };
    }

    public static Specification<Booking> hasItemId(long itemId) {
        return (root, query, builder) -> {
            Join<Booking, Item> item = root.join("item");
            return builder.equal(item.get("id"), itemId);
        };
    }

    public static Specification<Booking> hasState(BookingsState state) {
        return switch (state) {
            case ALL -> null;
            case CURRENT -> Specification
                    .where(hasStatusIn(List.of(BookingStatus.APPROVED)))
                    .and(started())
                    .and(Specification.not(finished()));
            case PAST -> Specification
                    .where(hasStatusIn(List.of(BookingStatus.APPROVED, BookingStatus.WAITING)))
                    .and(finished());
            case FUTURE -> Specification
                    .where(hasStatusIn(List.of(BookingStatus.APPROVED, BookingStatus.WAITING)))
                    .and(Specification.not(started()));
            case WAITING -> Specification.where(hasStatusIn(List.of(BookingStatus.WAITING)));
            case REJECTED -> Specification.where(hasStatusIn(List.of(BookingStatus.REJECTED)));
        };
    }
}
