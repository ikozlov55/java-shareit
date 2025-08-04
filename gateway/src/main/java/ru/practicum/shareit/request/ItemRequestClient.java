package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    private static final String REQUEST_ID_TEMPLATE = "/{requestId}";
    private static final String GET_ALL_REQUESTS_TEMPLATE = "/all";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItemRequestById(long requestId) {
        return get(REQUEST_ID_TEMPLATE, null, Map.of("requestId", requestId));
    }

    public ResponseEntity<Object> createItemRequest(long userId, ItemRequestDto request) {
        return post("", userId, request);
    }

    public ResponseEntity<Object> getUserItemRequests(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllItemRequests(long userId) {
        return get(GET_ALL_REQUESTS_TEMPLATE, userId);
    }
}
