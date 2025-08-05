package ru.practicum.shareit.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@TestComponent
@RequiredArgsConstructor
public class TestData {
    private final ShareItGatewayApi gatewayApi;
    private final ObjectMapper mapper;
    private final Faker faker = new Faker(Locale.of("RU"));

    public UserDto randomUser() throws Exception {
        UserDto user = new UserDto(null, faker.name().name(), faker.internet().emailAddress());
        return parse(gatewayApi.createUser(user).andReturn(), UserDto.class);
    }

    public ItemDto randomItem(long userId) throws Exception {
        ItemDto item = new ItemDto(null,
                faker.word().noun(),
                faker.lorem().paragraph(),
                true,
                null
        );
        return parse(gatewayApi.createItem(userId, item).andReturn(), ItemDto.class);
    }

    public long addBooking(long bookerId, long itemId) throws Exception {
        BookingCreateDto request = new BookingCreateDto(itemId,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        return getId(gatewayApi.createBooking(bookerId, request).andReturn());
    }

    public long addCompleteBooking(long bookerId, long ownerId, long itemId) throws Exception {
        BookingCreateDto request = new BookingCreateDto(itemId,
                LocalDateTime.now().plusSeconds(2),
                LocalDateTime.now().plusSeconds(4));
        long bookingId = getId(gatewayApi.createBooking(bookerId, request).andReturn());
        gatewayApi.approveBooking(ownerId, bookingId, true);
        TimeUnit.SECONDS.sleep(5);
        return bookingId;
    }

    public ItemRequestDto addItemRequest(long userId) throws Exception {
        ItemRequestDto request = new ItemRequestDto(null, faker.lorem().paragraph());
        return parse(gatewayApi.createItemRequest(userId, request).andReturn(), ItemRequestDto.class);
    }

    private <T> T parse(MvcResult result, Class<T> cls) throws Exception {
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return mapper.readValue(content, cls);
    }

    private int getId(MvcResult result) throws Exception {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }
}
