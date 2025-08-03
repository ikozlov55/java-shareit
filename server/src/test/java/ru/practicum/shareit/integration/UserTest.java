package ru.practicum.shareit.integration;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.testutils.ShareItServerApi;
import ru.practicum.shareit.testutils.TestData;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Locale;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ShareItServerApi.class, TestData.class})
public class UserTest {
    @Autowired
    private ShareItServerApi serverApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void testGetUserById() throws Exception {
        UserDto user = testData.randomUser();

        serverApi.getUserById(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto user = new UserDto(null, faker.name().name(), faker.internet().emailAddress());

        serverApi.createUser(user)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto user = testData.randomUser();
        user.setName(faker.name().name());
        user.setEmail(faker.internet().emailAddress());

        serverApi.updateUser(user.getId(), user)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
        serverApi.getUserById(user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testDeleteUser() throws Exception {
        UserDto user = testData.randomUser();
        long userId = user.getId();

        serverApi.deleteUser(userId).andExpect(status().isOk());
        serverApi.getUserById(userId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value(String.format("User %d not found!", userId)));
    }
}
