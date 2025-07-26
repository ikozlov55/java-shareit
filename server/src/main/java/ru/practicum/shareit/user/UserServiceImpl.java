package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.toDto(userRepository.findByIdOrThrow(userId));
    }

    @Override
    public UserDto createUser(UserDto user) {
        userRepository.throwIfEmailTaken(null, user.getEmail());
        User userModel = UserMapper.toModel(user);
        return UserMapper.toDto(userRepository.save(userModel));
    }

    @Override
    public UserDto updateUser(long userId, UserDto user) {
        User userModel = userRepository.findByIdOrThrow(userId);
        userRepository.throwIfEmailTaken(userId, user.getEmail());
        if (user.getName() != null && !user.getName().isBlank()) {
            userModel.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userModel.setEmail(user.getEmail());
        }
        return UserMapper.toDto(userRepository.save(userModel));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.findByIdOrThrow(userId);
        userRepository.deleteById(userId);
    }

}
