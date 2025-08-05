package ru.practicum.shareit.json;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.testutils.DtoValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
@Import(DtoValidator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTest {
    private final DtoValidator<BookingCreateDto> dtoValidator;
    private final JacksonTester<BookingCreateDto> jsonTester;

    @Test
    void testSerialize() throws IOException {
        BookingCreateDto dto = new BookingCreateDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        JsonContent<BookingCreateDto> json = jsonTester.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(dto.getItemId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.start")
                .isEqualTo(dto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathStringValue("$.end")
                .isEqualTo(dto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    void testDeserialize() throws IOException {
        @SuppressWarnings("RegexpSinglelineJava")
        String json = """
                {
                    "itemId":1,
                    "start":"2025-07-29T19:15:55.920783",
                    "end":"2025-07-30T19:15:55.920783"
                }
                """;
        BookingCreateDto dto = jsonTester.parseObject(json);

        assertThat(dto.getItemId()).isEqualTo(1);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.parse("2025-07-29T19:15:55.920783"));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.parse("2025-07-30T19:15:55.920783"));
    }

    @Test
    void testItemIdCantBeNull() {
        BookingCreateDto dto = new BookingCreateDto(null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        dtoValidator.assertHasError(dto, "itemId", "must not be null");
    }

    @Test
    void testStartDateMustBeInFuture() {
        BookingCreateDto dto = new BookingCreateDto(1L, LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusDays(2));

        dtoValidator.assertHasError(dto, "start", "must be a future date");
    }

    @Test
    void testEndDateMustBeInFuture() {
        BookingCreateDto dto = new BookingCreateDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusMinutes(5));

        dtoValidator.assertHasError(dto, "end", "must be a future date");
    }

}

