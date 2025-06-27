package ru.practicum.shareit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.configuration.DtoConfig;
import ru.practicum.shareit.configuration.JdbcConfig;

@Configuration
@Import({DtoConfig.class, JdbcConfig.class})
public class AppConfig {
}
