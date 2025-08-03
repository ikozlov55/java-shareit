package ru.practicum.shareit.json;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.testutils.DtoValidator;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
@Import(DtoValidator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestDtoTest {

    private final DtoValidator<ItemRequestDto> dtoValidator;
    private final JacksonTester<ItemRequestDto> jsonTester;

    @Test
    void testSerialize() throws IOException {
        ItemRequestDto dto = new ItemRequestDto(1L, "zzz");
        JsonContent<ItemRequestDto> json = jsonTester.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
    }

    @Test
    void testDeserialize() throws IOException {
        String json = "{\"id\": 1,\"description\":\"abcd\"}";
        ItemRequestDto dto = jsonTester.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getDescription()).isEqualTo("abcd");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void testDescriptionCantBeNull(String description) {
        ItemRequestDto dto = new ItemRequestDto(null, description);

        dtoValidator.assertHasError(dto, "description", "must not be blank");
    }
}
