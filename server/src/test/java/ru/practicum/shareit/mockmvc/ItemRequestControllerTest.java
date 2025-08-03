package ru.practicum.shareit.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItServerApi;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@Import(ShareItServerApi.class)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ShareItServerApi mockApi;


    @Test
    void testGetItemRequestByIdSuccess() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong()))
                .thenReturn(MocksData.ITEM_REQUEST);

        mockApi.getItemRequestById(1)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateItemRequestSuccess() throws Exception {
        when(itemRequestService.createItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(MocksData.ITEM_REQUEST);

        ItemRequestDto request = new ItemRequestDto(1L, "zzz", null, null);

        mockApi.createItemRequest(1, request)
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserItemRequestsSuccess() throws Exception {
        when(itemRequestService.getUserItemRequests(anyLong()))
                .thenReturn(List.of(MocksData.ITEM_REQUEST));

        mockApi.getUserItemRequests(1)
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllItemRequestsSuccess() throws Exception {
        when(itemRequestService.getAllItemRequests(anyLong()))
                .thenReturn(List.of(MocksData.ITEM_REQUEST));

        mockApi.getAllItemRequests(1)
                .andExpect(status().isOk());
    }
}
