package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";
    private static final String ITEM_ID_TEMPLATE = "/{itemId}";
    private static final String SEARCH_TEMPLATE = "/search?text={text}";
    private static final String ADD_COMMENT_TEMPLATE = "/{itemId}/comment";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItemById(long itemId) {
        return get(ITEM_ID_TEMPLATE, null, Map.of("itemId", itemId));
    }

    public ResponseEntity<Object> getUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> itemsSearch(long userId, String text) {
        return get(SEARCH_TEMPLATE, userId, Map.of("text", text));
    }

    public ResponseEntity<Object> createItem(long userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemDto item) {
        return patch(ITEM_ID_TEMPLATE, userId, Map.of("itemId", itemId), item);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentCreateDto comment) {
        return post(ADD_COMMENT_TEMPLATE, userId, Map.of("itemId", itemId), comment);
    }

}
