package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    void checkUserExists(long userId);

    User getUserById(long userId);

    User createUser(User user);

    User updateUser(long userId, User user);

    void deleteUser(long userId);
}
