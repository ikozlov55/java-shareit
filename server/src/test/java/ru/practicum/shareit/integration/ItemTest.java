package ru.practicum.shareit.integration;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.testutils.ShareItServerApi;
import ru.practicum.shareit.testutils.TestData;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ShareItServerApi.class, TestData.class})
public class ItemTest {
    @Autowired
    private ShareItServerApi serverApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void testGetItemById() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());

        serverApi.getItemById(item.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(item.getRequestId()));
    }

    @Test
    void testGetItemWithCommentById() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        testData.addCompleteBooking(booker.getId(), owner.getId(), item.getId());
        CommentDto comment = testData.addComment(booker.getId(), item.getId());
        serverApi.getItemById(item.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.comments[0].id").value(comment.getId()))
                .andExpect(jsonPath("$.comments[0].text").value(comment.getText()))
                .andExpect(jsonPath("$.comments[0].authorName").value(booker.getName()));
    }

    @Test
    void testGetItemByNonexistentId() throws Exception {
        serverApi.getItemById(9999)
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetUserItems() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item1 = testData.randomItem(user.getId());
        ItemDto item2 = testData.randomItem(user.getId());

        serverApi.getUserItems(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(item1.getId()))
                .andExpect(jsonPath("$.[1].id").value(item2.getId()));
    }

    @Test
    void testGetUserItemsWithBookingData() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        BookingDto lastBooking = testData.addCompleteBooking(booker.getId(), owner.getId(), item.getId());
        BookingDto nextBooking = testData.addBooking(booker.getId(), item.getId());

        serverApi.getUserItems(owner.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].lastBooking.id").value(lastBooking.getId()))
                .andExpect(jsonPath("$.[0].nextBooking.id").value(nextBooking.getId()));
    }

    @Test
    void testGetUserItemsByNonexistentId() throws Exception {
        serverApi.getUserItems(9999)
                .andExpect(status().isNotFound());
    }

    @Test
    void testItemsSearch() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());

        serverApi.itemsSearch(user.getId(), item.getName())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name", everyItem(containsString(item.getName()))));
    }

    @Test
    void testItemsSearchWithBlankTextIsEmpty() throws Exception {
        UserDto user = testData.randomUser();

        serverApi.itemsSearch(user.getId(), "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }


    @Test
    void testCreateItem() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = new ItemDto(null,
                faker.word().noun(),
                faker.lorem().paragraph(),
                true,
                null,
                null,
                null,
                null);

        serverApi.createItem(user.getId(), item)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(item.getRequestId()));
    }

    @Test
    void testCreateItemByRequest() throws Exception {
        UserDto user = testData.randomUser();
        ItemRequestDto request = testData.addItemRequest(user.getId());
        ItemDto item = new ItemDto(null,
                faker.word().noun(),
                faker.lorem().paragraph(),
                true,
                request.getId(),
                null,
                null,
                null);

        serverApi.createItem(user.getId(), item)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(item.getRequestId()));
    }

    @Test
    void testCreateItemByNonexistentUserReturnsError() throws Exception {
        ItemDto item = new ItemDto(null,
                faker.word().noun(),
                faker.lorem().paragraph(),
                true,
                null,
                null,
                null,
                null);

        serverApi.createItem(9999, item)
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateItem() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());
        item.setName(faker.word().noun());
        item.setDescription(faker.lorem().paragraph());
        item.setAvailable(!item.getAvailable());

        serverApi.updateItem(user.getId(), item.getId(), item)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));
    }

    @Test
    void testOnlyOwnerCanUpdateItem() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto user = testData.randomUser();

        serverApi.updateItem(user.getId(), item.getId(), item)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.reason").value("Item update request denied!"));
    }

    @Test
    void testUpdateItemByNonexistentUserReturnsError() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());

        serverApi.updateItem(9999, item.getId(), item)
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateNonexistentItemReturnsError() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());

        serverApi.updateItem(user.getId(), 9999, item)
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddComment() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        testData.addCompleteBooking(booker.getId(), owner.getId(), item.getId());
        CommentCreateDto request = new CommentCreateDto(faker.lorem().paragraph());

        serverApi.addComment(booker.getId(), item.getId(), request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.text").value(request.getText()))
                .andExpect(jsonPath("$.authorName").value(booker.getName()));
    }

    @Test
    void testAddCommentWithNonexistentUserReturnsError() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());
        CommentCreateDto request = new CommentCreateDto(faker.lorem().paragraph());

        serverApi.addComment(9999, item.getId(), request)
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCommentWithNonexistentItemReturnsError() throws Exception {
        UserDto user = testData.randomUser();
        CommentCreateDto request = new CommentCreateDto(faker.lorem().paragraph());

        serverApi.addComment(user.getId(), 9999, request)
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCommentIsAvailableForBookedItems() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto user = testData.randomUser();
        CommentCreateDto request = new CommentCreateDto(faker.lorem().paragraph());
        String error = String.format("Your booking of item %d not found, or has invalid state!", item.getId());

        serverApi.addComment(user.getId(), item.getId(), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value(error));
    }

    @Test
    void testAddCommentAvailableOnlyOnce() throws Exception {
        UserDto owner = testData.randomUser();
        ItemDto item = testData.randomItem(owner.getId());
        UserDto booker = testData.randomUser();
        testData.addCompleteBooking(booker.getId(), owner.getId(), item.getId());
        CommentCreateDto request = new CommentCreateDto(faker.lorem().paragraph());
        String error = "You have already left a comment for this item!";

        serverApi.addComment(booker.getId(), item.getId(), request)
                .andExpect(status().isOk());

        serverApi.addComment(booker.getId(), item.getId(), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value(error));
    }
}
