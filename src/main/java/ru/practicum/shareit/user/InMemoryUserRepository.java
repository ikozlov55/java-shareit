package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static Long nextEntityId = 1L;
    private final Map<Long, User> usersMap = new HashMap<>();

    @Override
    public void checkUserExists(long userId) {
        if (usersMap.containsKey(userId)) {
            return;
        }
        throw new NotFoundException(String.format("User %d not found!", userId));
    }

    @Override
    public User getUserById(long userId) {
        checkUserExists(userId);
        return usersMap.get(userId);
    }

    @Override
    public User createUser(User user) {
        checkEmailUnique(null, user.getEmail());
        user.setId(nextEntityId);
        usersMap.put(nextEntityId, user);
        nextEntityId++;
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        checkUserExists(userId);
        checkEmailUnique(userId, user.getEmail());
        User storedUser = usersMap.get(userId);
        if (user.getName() != null) {
            storedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            storedUser.setEmail(user.getEmail());
        }
        return storedUser;
    }

    @Override
    public void deleteUser(long userId) {
        checkUserExists(userId);
        usersMap.remove(userId);
    }

    private void checkEmailUnique(Long userId, String email) {
        for (User storedUser : usersMap.values()) {
            if (!storedUser.getEmail().equals(email)) {
                continue;
            }
            if (userId == null || !userId.equals(storedUser.getId())) {
                throw new ConflictException(String.format("User with email %s already registered!", email));
            }
        }
    }
}
