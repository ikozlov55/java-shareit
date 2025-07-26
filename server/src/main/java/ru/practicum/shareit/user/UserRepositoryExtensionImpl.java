package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;


public class UserRepositoryExtensionImpl implements UserRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    public User findByIdOrThrow(long userId) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("User %d not found!", userId)));
    }

    @Override
    public void throwIfEmailTaken(Long userId, String email) {
        Long count = entityManager.createQuery("""
                        SELECT COUNT(u)
                          FROM User u
                         WHERE u.email = :email
                           AND u.id != :userId
                        """, Long.class)
                .setParameter("email", email)
                .setParameter("userId", userId != null ? userId : 0)
                .getSingleResult();
        if (count > 0) {
            throw new ConflictException(String.format("User with email %s already registered!", email));
        }
    }
}
