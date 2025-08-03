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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItGatewayApi;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
@Import(ShareItGatewayApi.class)
@ContextConfiguration(classes = ShareItGateway.class)
public class ItemControllerTest {
    @MockBean
    private ItemClient itemClient;

    @Autowired
    private ShareItGatewayApi mockApi;

    @Test
    void testGetItemByIdSuccess() throws Exception {
        when(itemClient.getItemById(anyLong()))
                .thenReturn(new ResponseEntity<>(MocksData.ITEM, HttpStatus.OK));

        mockApi.getItemById(1).andExpect(status().isOk());
    }

    @Test
    void testGetUserItemsSuccess() throws Exception {
        when(itemClient.getUserItems(anyLong()))
                .thenReturn(new ResponseEntity<>("[" + MocksData.ITEM + "]", HttpStatus.OK));

        mockApi.getUserItems(1).andExpect(status().isOk());
    }

    @Test
    void testItemsSearchSuccess() throws Exception {
        when(itemClient.itemsSearch(anyLong(), anyString()))
                .thenReturn(new ResponseEntity<>("[" + MocksData.ITEM + "]", HttpStatus.OK));

        mockApi.itemsSearch(1, "zzz").andExpect(status().isOk());
    }

    @Test
    void testCreateItemSuccess() throws Exception {
        when(itemClient.createItem(anyLong(), any(ItemDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.ITEM, HttpStatus.OK));

        ItemDto request = new ItemDto(1L, "Item name", "Item description",
                true, null);

        mockApi.createItem(1, request).andExpect(status().isOk());
    }

    @Test
    void testCreateItemNameIsRequired() throws Exception {
        ItemDto request = new ItemDto(1L, null, "Item description",
                true, null);

        mockApi.createItem(1, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is("Item name is required!")));
    }

    @Test
    void testCreateItemDescriptionIsRequired() throws Exception {
        ItemDto request = new ItemDto(1L, "Item name", null,
                true, null);

        mockApi.createItem(1, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is("Item description is required!")));
    }

    @Test
    void testCreateItemAvailabilityIsRequired() throws Exception {
        ItemDto request = new ItemDto(1L, "Item name", "Item description",
                null, null);

        mockApi.createItem(1, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is("Item availability is required!")));
    }

    @Test
    void testUpdateItemSuccess() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.ITEM, HttpStatus.OK));

        ItemDto request = new ItemDto(1L, "Item name", "Item description",
                true, null);

        mockApi.updateItem(1, 1, request).andExpect(status().isOk());
    }

    @Test
    void testAddCommentSuccess() throws Exception {
        when(itemClient.addComment(anyLong(), anyLong(), any(CommentCreateDto.class)))
                .thenReturn(new ResponseEntity<>(MocksData.COMMENT, HttpStatus.OK));

        CommentCreateDto request = new CommentCreateDto("zzz");

        mockApi.addComment(1, 1, request).andExpect(status().isOk());
    }
}
