package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

public interface UserRepositoryExtension {
    User findByIdOrThrow(long userId);

    void throwIfEmailTaken(Long userId, String email);
}
