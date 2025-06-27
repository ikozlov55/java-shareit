package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toModel(UserDto user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }
}
