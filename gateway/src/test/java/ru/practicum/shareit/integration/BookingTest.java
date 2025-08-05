package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingsState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.testutils.ShareItGatewayApi;
import ru.practicum.shareit.testutils.TestData;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@Import({ShareItGatewayApi.class, TestData.class})
@ContextConfiguration(classes = ShareItGateway.class)
public class BookingTest {
    @Autowired
    private ShareItGatewayApi gatewayApi;
    @Autowired
    private TestData testData;

    @Test
    void testCreateBooking() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        BookingCreateDto request = new BookingCreateDto(item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        String start = request.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String end = request.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        gatewayApi.createBooking(booker.getId(), request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.start").value(start))
                .andExpect(jsonPath("$.end").value(end))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void testApproveBooking() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        long bookingId = testData.addBooking(booker.getId(), item.getId());

        gatewayApi.approveBooking(owner.getId(), bookingId, true)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testGetBookingById() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        long bookingId = testData.addBooking(booker.getId(), item.getId());

        gatewayApi.getBookingById(owner.getId(), bookingId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void testGetBookings() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item1 = testData.randomItem(owner.getId());
        ItemDto item2 = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        long bookingId1 = testData.addBooking(booker.getId(), item1.getId());
        long bookingId2 = testData.addBooking(booker.getId(), item2.getId());

        gatewayApi.getBookings(booker.getId(), BookingsState.ALL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(bookingId2))
                .andExpect(jsonPath("$.[0].item.id").value(item2.getId()))
                .andExpect(jsonPath("$.[1].id").value(bookingId1))
                .andExpect(jsonPath("$.[1].item.id").value(item1.getId()));
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item1 = testData.randomItem(owner.getId());
        ItemDto item2 = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        long bookingId1 = testData.addBooking(booker.getId(), item1.getId());
        long bookingId2 = testData.addBooking(booker.getId(), item2.getId());

        gatewayApi.getOwnerBookings(owner.getId(), BookingsState.ALL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(bookingId1))
                .andExpect(jsonPath("$.[0].item.id").value(item1.getId()))
                .andExpect(jsonPath("$.[1].id").value(bookingId2))
                .andExpect(jsonPath("$.[1].item.id").value(item2.getId()));
    }
}
