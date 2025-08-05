package ru.practicum.shareit.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingsState;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItGatewayApi;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ShareItGateway.class)
@WebMvcTest(controllers = BookingController.class)
@Import(ShareItGatewayApi.class)
public class BookingControllerTest {
    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ShareItGatewayApi mockApi;


    @Test
    void testCreateBookingSuccess() throws Exception {
        when(bookingClient.createBooking(anyLong(), any(BookingCreateDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.BOOKING, HttpStatus.OK));

        BookingCreateDto request = new BookingCreateDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        mockApi.createBooking(1, request)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateBookingWithInvalidTimeInterval() throws Exception {
        BookingCreateDto request = new BookingCreateDto(1L, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(4));

        mockApi.createBooking(1, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is("Invalid booking interval!")));
    }

    @Test
    void testApproveBookingSuccess() throws Exception {
        when(bookingClient.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new ResponseEntity<>(MocksData.BOOKING, HttpStatus.OK));

        mockApi.approveBooking(1, 1, true)
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingSuccess() throws Exception {
        when(bookingClient.getBookingById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(MocksData.BOOKING, HttpStatus.OK));

        mockApi.getBookingById(1, 1)
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingsSuccess() throws Exception {
        when(bookingClient.getBookings(anyLong(), any(BookingsState.class)))
                .thenReturn(new ResponseEntity<>("[" + MocksData.BOOKING + "]", HttpStatus.OK));

        mockApi.getBookings(1, BookingsState.ALL)
                .andExpect(status().isOk());
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        when(bookingClient.getOwnerBookings(anyLong(), any(BookingsState.class)))
                .thenReturn(new ResponseEntity<>("[" + MocksData.BOOKING + "]", HttpStatus.OK));

        mockApi.getOwnerBookings(1, BookingsState.ALL)
                .andExpect(status().isOk());
    }
}
