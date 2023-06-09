package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull String test,
    Scheduler scheduler,
    @Name("database-access-type") AccessType accessType,
    @NotNull String queueName,
    @NotNull String exchangeName,
    @NotNull String routingKey,
    @NotNull boolean useQueue
) {

    public record Scheduler(Duration interval) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
