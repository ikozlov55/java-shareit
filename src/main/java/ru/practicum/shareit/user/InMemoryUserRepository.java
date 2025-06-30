package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static Long nextEntityId = 1L;
    private final Map<Long, User> usersMap = new HashMap<>();
    private final Map<String, Long> userEmailToIdMap = new HashMap<>();

    @Override
    public void checkUserExists(long userId) {
        if (!usersMap.containsKey(userId)) {
            throw new NotFoundException(String.format("User %d not found!", userId));
        }
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
        userEmailToIdMap.put(user.getEmail(), nextEntityId);
        nextEntityId++;
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        checkUserExists(userId);
        checkEmailUnique(userId, user.getEmail());
        User storedUser = usersMap.get(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            storedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userEmailToIdMap.remove(storedUser.getEmail());
            storedUser.setEmail(user.getEmail());
            userEmailToIdMap.put(storedUser.getEmail(), storedUser.getId());
        }
        return storedUser;
    }

    @Override
    public void deleteUser(long userId) {
        checkUserExists(userId);
        User user = usersMap.remove(userId);
        userEmailToIdMap.remove(user.getEmail());
    }

    private void checkEmailUnique(Long userId, String email) {
        if (userEmailToIdMap.containsKey(email) && !userEmailToIdMap.get(email).equals(userId)) {
            throw new ConflictException(String.format("User with email %s already registered!", email));
        }
    }
}
