package ru.practicum.shareit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.configuration.WebConfig;

@Configuration
@Import(WebConfig.class)
public class AppConfig {
}
