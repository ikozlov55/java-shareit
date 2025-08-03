package ru.practicum.shareit.testutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.Validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestComponent
public class DtoValidator<T> {
    @Autowired
    private Validator validator;

    public void assertHasError(T dto, String fieldName, String message) {
        SimpleErrors errors = new SimpleErrors(dto);
        validator.validate(dto, errors);
        assertThat(errors.hasFieldErrors(fieldName), is(true));
        assertThat(errors.getFieldError(fieldName).getDefaultMessage(), is(message));
    }

}
