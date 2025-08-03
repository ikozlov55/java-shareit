package ru.practicum.shareit.integration;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
    void testItemsSearch() throws Exception {
        UserDto user = testData.randomUser();
        ItemDto item = testData.randomItem(user.getId());

        serverApi.itemsSearch(user.getId(), item.getName())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name", everyItem(containsString(item.getName()))));
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
}
