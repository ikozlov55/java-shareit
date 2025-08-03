package ru.practicum.shareit.integration;

import net.datafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.testutils.ShareItGatewayApi;
import ru.practicum.shareit.testutils.TestData;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@Import({ShareItGatewayApi.class, TestData.class})
@ContextConfiguration(classes = ShareItGateway.class)
public class ItemRequestTest {
    @Autowired
    private ShareItGatewayApi gatewayApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void testCreateItemRequest() throws Exception {
        UserDto user = testData.randomUser();
        ItemRequestDto request = new ItemRequestDto(null, faker.lorem().paragraph());

        gatewayApi.createItemRequest(user.getId(), request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.created").value(notNullValue()));
    }

    @Test
    void testGetItemRequestById() throws Exception {
        UserDto user = testData.randomUser();
        ItemRequestDto request = testData.addItemRequest(user.getId());

        gatewayApi.getItemRequestById(request.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    @Test
    void testGetUserItemRequests() throws Exception {
        UserDto user = testData.randomUser();
        ItemRequestDto request1 = testData.addItemRequest(user.getId());
        ItemRequestDto request2 = testData.addItemRequest(user.getId());

        gatewayApi.getUserItemRequests(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].id",
                        containsInAnyOrder(request1.getId().intValue(), request2.getId().intValue())));
    }

    @Test
    void testGetAllItemRequests() throws Exception {
        UserDto user1 = testData.randomUser();
        testData.addItemRequest(user1.getId());
        testData.addItemRequest(user1.getId());
        UserDto user2 = testData.randomUser();
        testData.addItemRequest(user2.getId());
        testData.addItemRequest(user2.getId());

        gatewayApi.getAllItemRequests(user1.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))));
    }
}
