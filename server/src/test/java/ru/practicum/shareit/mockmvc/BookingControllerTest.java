package ru.practicum.shareit.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.BookingsState;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItServerApi;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@Import(ShareItServerApi.class)
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private ShareItServerApi mockApi;


    @Test
    void testCreateBookingSuccess() throws Exception {
        when(bookingService.createBooking(anyLong(), any(BookingCreateDto.class)))
                .thenReturn(MocksData.BOOKING);

        BookingCreateDto request = new BookingCreateDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        mockApi.createBooking(1, request)
                .andExpect(status().isOk());
    }

    @Test
    void testApproveBookingSuccess() throws Exception {
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(MocksData.BOOKING);

        mockApi.approveBooking(1, 1, true)
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingSuccess() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(MocksData.BOOKING);

        mockApi.getBookingById(1, 1)
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingsSuccess() throws Exception {
        when(bookingService.getBookings(anyLong(), any(BookingsState.class)))
                .thenReturn(List.of(MocksData.BOOKING));

        mockApi.getBookings(1, BookingsState.ALL)
                .andExpect(status().isOk());
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), any(BookingsState.class)))
                .thenReturn(List.of(MocksData.BOOKING));

        mockApi.getOwnerBookings(1, BookingsState.ALL)
                .andExpect(status().isOk());
    }
}
