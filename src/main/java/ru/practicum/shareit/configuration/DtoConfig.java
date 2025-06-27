package ru.practicum.shareit.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@Configuration
public class DtoConfig {
    @Bean
    public ItemMapper itemMapper() {
        return new ItemMapper();
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
}
