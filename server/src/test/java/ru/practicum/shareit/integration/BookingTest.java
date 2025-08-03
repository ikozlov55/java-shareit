package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingsState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.testutils.ShareItServerApi;
import ru.practicum.shareit.testutils.TestData;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ShareItServerApi.class, TestData.class})
public class BookingTest {
    @Autowired
    private ShareItServerApi serverApi;
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

        serverApi.createBooking(booker.getId(), request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.start").value(start))
                .andExpect(jsonPath("$.end").value(end))
                .andExpect(jsonPath("$.status").value(BookingStatus.WAITING.name()));
    }

    @Test
    void testApproveBooking() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        BookingDto booking = testData.addBooking(booker.getId(), item.getId());

        serverApi.approveBooking(owner.getId(), booking.getId(), true)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.status").value(BookingStatus.APPROVED.name()));
    }

    @Test
    void testGetBookingById() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        BookingDto booking = testData.addBooking(booker.getId(), item.getId());

        serverApi.getBookingById(owner.getId(), booking.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.status").value(BookingStatus.WAITING.name()));
    }

    @Test
    void testGetBookings() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item1 = testData.randomItem(owner.getId());
        ItemDto item2 = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        BookingDto booking1 = testData.addBooking(booker.getId(), item1.getId());
        BookingDto booking2 = testData.addBooking(booker.getId(), item2.getId());

        serverApi.getBookings(booker.getId(), BookingsState.ALL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(booking2.getId()))
                .andExpect(jsonPath("$.[0].item.id").value(item2.getId()))
                .andExpect(jsonPath("$.[1].id").value(booking1.getId()))
                .andExpect(jsonPath("$.[1].item.id").value(item1.getId()));
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item1 = testData.randomItem(owner.getId());
        ItemDto item2 = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        BookingDto booking1 = testData.addBooking(booker.getId(), item1.getId());
        BookingDto booking2 = testData.addBooking(booker.getId(), item2.getId());

        serverApi.getOwnerBookings(owner.getId(), BookingsState.ALL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(booking1.getId()))
                .andExpect(jsonPath("$.[0].item.id").value(item1.getId()))
                .andExpect(jsonPath("$.[1].id").value(booking2.getId()))
                .andExpect(jsonPath("$.[1].item.id").value(item2.getId()));
    }
}
