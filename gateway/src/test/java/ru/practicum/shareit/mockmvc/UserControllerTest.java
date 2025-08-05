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
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItGatewayApi;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@Import(ShareItGatewayApi.class)
@ContextConfiguration(classes = ShareItGateway.class)
public class UserControllerTest {
    @MockBean
    private UserClient userClient;

    @Autowired
    private ShareItGatewayApi mockApi;

    @Test
    void testGetUserByIdSuccess() throws Exception {
        when(userClient.getUserById(anyLong()))
                .thenReturn(new ResponseEntity<>(MocksData.USER, HttpStatus.OK));

        mockApi.getUserById(1)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        when(userClient.createUser(any(UserDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.USER, HttpStatus.OK));

        UserDto request = new UserDto(1L, "Владимир Владимиров", "vladimir@mail.ru");

        mockApi.createUser(request)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserNameIsRequired() throws Exception {
        UserDto request = new UserDto(1L, null, "vladimir@mail.ru");

        mockApi.createUser(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is("User name is required!")));
    }

    @Test
    void testCreateEmailIsRequired() throws Exception {
        UserDto request = new UserDto(1L, "Владимир Владимиров", null);

        mockApi.createUser(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is("User email is required!")));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        when(userClient.updateUser(anyLong(), any(UserDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.USER, HttpStatus.OK));

        UserDto request = new UserDto(null, "Владимир Владимиров", "vladimir@mail.ru");

        mockApi.updateUser(1, request)
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        mockApi.deleteUser(1)
                .andExpect(status().isOk());
    }
}
