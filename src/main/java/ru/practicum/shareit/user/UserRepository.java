package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryExtension {
    Optional<User> findByEmail(String email);
}
