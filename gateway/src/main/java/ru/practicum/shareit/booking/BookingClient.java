package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingsState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    private static final String BOOKING_ID_TEMPLATE = "/{bookingId}";
    private static final String APPROVE_BOOKING_TEMPLATE = "/{bookingId}?approved={approved}";
    private static final String GET_BOOKINGS_TEMPLATE = "?state={state}";
    private static final String GET_OWNER_BOOKINGS_TEMPLATE = "/owner?state={state}";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingCreateDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> approveBooking(long userId, long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved);
        return patch(APPROVE_BOOKING_TEMPLATE, userId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return get(BOOKING_ID_TEMPLATE, userId, Map.of("bookingId", bookingId));
    }

    public ResponseEntity<Object> getBookings(long userId, BookingsState state) {
        return get(GET_BOOKINGS_TEMPLATE, userId, Map.of("state", state.name()));
    }

    public ResponseEntity<Object> getOwnerBookings(long userId, BookingsState state) {
        return get(GET_OWNER_BOOKINGS_TEMPLATE, userId, Map.of("state", state.name()));
    }
}