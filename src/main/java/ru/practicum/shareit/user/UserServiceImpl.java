package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(long userId) {
        User userModel = userRepository.getUserById(userId);
        return UserMapper.toDto(userModel);
    }

    @Override
    public UserDto createUser(UserDto user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("User name is required!");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("User email is required!");
        }
        User userModel = UserMapper.toModel(user);
        return UserMapper.toDto(userRepository.createUser(userModel));
    }

    @Override
    public UserDto updateUser(long userId, UserDto user) {
        User userModel = UserMapper.toModel(user);
        return UserMapper.toDto(userRepository.updateUser(userId, userModel));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }
}
