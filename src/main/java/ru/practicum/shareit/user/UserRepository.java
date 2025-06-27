package ru.practicum.shareit.user;

public interface UserRepository {
    void checkUserExists(long userId);

    User getUserById(long userId);

    User createUser(User user);

    User updateUser(long userId, User user);

    void deleteUser(long userId);
}
