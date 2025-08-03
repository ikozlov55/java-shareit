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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.testutils.DtoValidator;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
@Import(DtoValidator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentDtoTest {
    private final DtoValidator<CommentCreateDto> dtoValidator;
    private final JacksonTester<CommentCreateDto> jsonTester;

    @Test
    void testSerialize() throws IOException {
        CommentCreateDto dto = new CommentCreateDto("zzz");
        JsonContent<CommentCreateDto> json = jsonTester.write(dto);

        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());

    }

    @Test
    void testDeserialize() throws IOException {
        String json = "{\"text\":\"abcd\"}";
        CommentCreateDto dto = jsonTester.parseObject(json);

        assertThat(dto.getText()).isEqualTo("abcd");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void testCommentTextCantBeNull(String text) {
        CommentCreateDto dto = new CommentCreateDto(text);

        dtoValidator.assertHasError(dto, "text", "must not be blank");
    }
}
