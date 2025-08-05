package ru.practicum.shareit.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.testutils.MocksData;
import ru.practicum.shareit.testutils.ShareItServerApi;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@Import(ShareItServerApi.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private ShareItServerApi mockApi;


    @Test
    void testGetItemByIdSuccess() throws Exception {
        when(itemService.getItemById(anyLong()))
                .thenReturn(MocksData.ITEM);

        mockApi.getItemById(1).andExpect(status().isOk());
    }

    @Test
    void testGetUserItemsSuccess() throws Exception {
        when(itemService.getUserItems(anyLong()))
                .thenReturn(List.of(MocksData.ITEM));

        mockApi.getUserItems(1).andExpect(status().isOk());
    }

    @Test
    void testItemsSearchSuccess() throws Exception {
        when(itemService.itemsSearch(anyLong(), anyString()))
                .thenReturn(List.of(MocksData.ITEM));

        mockApi.itemsSearch(1, "zzz").andExpect(status().isOk());
    }

    @Test
    void testCreateItemSuccess() throws Exception {
        when(itemService.createItem(anyLong(), any(ItemDto.class)))
                .thenReturn(MocksData.ITEM);

        ItemDto request = new ItemDto(1L, "Item name", "Item description",
                true, null, null, null, null);

        mockApi.createItem(1, request).andExpect(status().isOk());
    }

    @Test
    void testUpdateItemSuccess() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(MocksData.ITEM);

        ItemDto request = new ItemDto(1L, "Item name", "Item description",
                true, null, null, null, null);

        mockApi.updateItem(1, 1, request).andExpect(status().isOk());
    }

    @Test
    void testAddCommentSuccess() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentCreateDto.class)))
                .thenReturn(MocksData.COMMENT);

        CommentCreateDto request = new CommentCreateDto("zzz");

        mockApi.addComment(1, 1, request).andExpect(status().isOk());
    }
}
