package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        if (isReachable()) {
            return Health.up()
                    .status(Status.UP)
                    .withDetail("message", "All is fine!")
                    .build();
        } else {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Something goes wrong. Please check!")
                    .build();
        }
    }

    public boolean isReachable() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.execute("select NAME from BOOKS");
        } catch (SQLException e) {
            return false;
        }
    }
}
