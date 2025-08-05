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
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItGatewayApi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
@Import(ShareItGatewayApi.class)
@ContextConfiguration(classes = ShareItGateway.class)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private ShareItGatewayApi mockApi;

    @Test
    void testGetItemRequestByIdSuccess() throws Exception {
        when(itemRequestClient.getItemRequestById(anyLong()))
                .thenReturn(new ResponseEntity<>(MocksData.ITEM_REQUEST, HttpStatus.OK));

        mockApi.getItemRequestById(1)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateItemRequestSuccess() throws Exception {
        when(itemRequestClient.createItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.ITEM_REQUEST, HttpStatus.OK));

        ItemRequestDto request = new ItemRequestDto(1L, "zzz");

        mockApi.createItemRequest(1, request)
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserItemRequestsSuccess() throws Exception {
        when(itemRequestClient.getUserItemRequests(anyLong()))
                .thenReturn(new ResponseEntity<>("[" + MocksData.ITEM_REQUEST + "]", HttpStatus.OK));

        mockApi.getUserItemRequests(1)
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllItemRequestsSuccess() throws Exception {
        when(itemRequestClient.getAllItemRequests(anyLong()))
                .thenReturn(new ResponseEntity<>("[" + MocksData.ITEM_REQUEST + "]", HttpStatus.OK));

        mockApi.getAllItemRequests(1)
                .andExpect(status().isOk());
    }
}
