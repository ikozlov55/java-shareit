package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto getUserById(long userId);

    UserDto createUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);
}
