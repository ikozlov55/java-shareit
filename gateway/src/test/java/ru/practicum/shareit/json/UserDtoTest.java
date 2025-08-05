package ru.practicum.shareit.json;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.testutils.DtoValidator;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
@Import(DtoValidator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoTest {
    private final DtoValidator<UserDto> dtoValidator;
    private final JacksonTester<UserDto> jsonTester;

    @Test
    void testSerialize() throws IOException {
        UserDto dto = new UserDto(1L, "Владимир Владимиров", "vladimir@mail.ru");
        JsonContent<UserDto> json = jsonTester.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo(dto.getEmail());
    }

    @Test
    void testDeserialize() throws IOException {
        @SuppressWarnings("RegexpSinglelineJava")
        String json = """
                {
                    "id": 1,
                    "name":"Владимир Владимиров",
                    "email": "vladimir@mail.ru"
                }
                """;
        UserDto dto = jsonTester.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("Владимир Владимиров");
        assertThat(dto.getEmail()).isEqualTo("vladimir@mail.ru");
    }


    @ParameterizedTest
    @ValueSource(strings = {"@mail.ru", "mail"})
    void testUserEmailMustBeWellFormed(String email) {
        UserDto dto = new UserDto(1L, "Владимир Владимиров", email);

        dtoValidator.assertHasError(dto, "email", "must be a well-formed email address");
    }
}
