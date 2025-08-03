package ru.practicum.shareit.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItServerApi;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(ShareItServerApi.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private ShareItServerApi mockApi;


    @Test
    void testGetUserByIdSuccess() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(MocksData.USER);

        mockApi.getUserById(1)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        when(userService.createUser(any(UserDto.class)))
                .thenReturn(MocksData.USER);

        UserDto request = new UserDto(1L, "Владимир Владимиров", "vladimir@mail.ru");

        mockApi.createUser(request)
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDto.class)))
                .thenReturn(MocksData.USER);

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
