package ru.practicum.shareit.user.dto;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    @Getter
    private static final UserMapper instance = new UserMapper();

    private UserMapper() {
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toModel(UserDto user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }
}
