package by.aab.isp.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.experimental.Delegate;

@Component
public class HikariConfig implements Map<String, String> {

    private static final Map<String, String> MAP_TO_HIKARI = Map.of(
            "db.url", "hibernate.hikari.jdbcUrl",
            "db.user", "hibernate.hikari.username",
            "db.password", "hibernate.hikari.password",
            "db.poolsize", "hibernate.hikari.maximumPoolSize"
    );

    @Delegate
    private final Map<String, String> map;

    public HikariConfig(ConfigManager config) {
        map = MAP_TO_HIKARI.entrySet()
                .stream()
                .filter(prop -> config.getString(prop.getKey()) != null)
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getValue, prop -> config.getString(prop.getKey())));
    }
}
