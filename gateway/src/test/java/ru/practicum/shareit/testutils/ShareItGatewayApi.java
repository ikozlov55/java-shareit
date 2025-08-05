package ru.practicum.shareit.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingsState;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestComponent
@RequiredArgsConstructor
public class ShareItGatewayApi {
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;


    public ResultActions createBooking(long userId, BookingCreateDto dto) throws Exception {
        return mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(Constants.USER_ID_HEADER, userId)
                .content(mapper.writeValueAsString(dto)));
    }

    public ResultActions approveBooking(long userId, long bookingId, boolean approved) throws Exception {
        return mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions getBookingById(long userId, long bookingId) throws Exception {
        return mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions getBookings(long userId, BookingsState state) throws Exception {
        return mockMvc.perform(get("/bookings?state={state}", state)
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions getOwnerBookings(long userId, BookingsState state) throws Exception {
        return mockMvc.perform(get("/bookings/owner?state={state}", state)
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions getItemById(long itemId) throws Exception {
        return mockMvc.perform(get("/items/{itemId}", itemId));
    }


    public ResultActions getUserItems(long userId) throws Exception {
        return mockMvc.perform(get("/items")
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions itemsSearch(long userId, String text) throws Exception {
        return mockMvc.perform(get("/items/search?text={text}", text)
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions createItem(long userId, ItemDto item) throws Exception {
        return mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(Constants.USER_ID_HEADER, userId)
                .content(mapper.writeValueAsString(item)));
    }

    public ResultActions updateItem(long userId, long itemId, ItemDto item) throws Exception {
        return mockMvc.perform(patch("/items/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(Constants.USER_ID_HEADER, userId)
                .content(mapper.writeValueAsString(item)));
    }

    public ResultActions addComment(long userId, long itemId, CommentCreateDto comment) throws Exception {
        return mockMvc.perform(post("/items/{itemId}/comment", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(Constants.USER_ID_HEADER, userId)
                .content(mapper.writeValueAsString(comment)));
    }

    public ResultActions getItemRequestById(long requestId) throws Exception {
        return mockMvc.perform(get("/requests/{requestId}", requestId));
    }

    public ResultActions createItemRequest(long userId, ItemRequestDto request) throws Exception {
        return mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(Constants.USER_ID_HEADER, userId)
                .content(mapper.writeValueAsString(request)));
    }

    public ResultActions getUserItemRequests(long userId) throws Exception {
        return mockMvc.perform(get("/requests")
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions getAllItemRequests(long userId) throws Exception {
        return mockMvc.perform(get("/requests/all")
                .header(Constants.USER_ID_HEADER, userId));
    }

    public ResultActions getUserById(long userId) throws Exception {
        return mockMvc.perform(get("/users/{userId}", userId));
    }

    public ResultActions createUser(UserDto user) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));
    }

    public ResultActions updateUser(long userId, UserDto user) throws Exception {
        return mockMvc.perform(patch("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));
    }

    public ResultActions deleteUser(long userId) throws Exception {
        return mockMvc.perform(delete("/users/{userId}", userId));
    }

}
