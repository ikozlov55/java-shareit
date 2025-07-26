package ru.practicum.shareit.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingsState;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class BookingStateEnumConverter implements Converter<String, BookingsState> {
    @Override
    public BookingsState convert(String value) {
        try {
            return BookingsState.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Invalid booking state: %s!", value));
        }
    }
}
