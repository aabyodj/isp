package by.aab.isp.repository.datasource;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

import by.aab.isp.config.ConfigManager;
import lombok.Getter;

@Component
public class DataSourceJdbc extends HikariDataSource {
	
    private static final int DEFAULT_POOL_SIZE = 2;
    private static final int MINIMAL_POOL_SIZE = 2;
    private static final Pattern SCHEMA_PATTERN = Pattern.compile("^jdbc:([a-z]+):");
    
    @Getter
    private final SqlDialect dialect;

	public DataSourceJdbc(ConfigManager config) {
        setJdbcUrl(config.getString("db.url"));
        setUsername(config.getString("db.user"));
        setPassword(config.getString("db.password"));
        setMaximumPoolSize(Integer.max(
                config.getInt("db.poolsize", DEFAULT_POOL_SIZE),
                MINIMAL_POOL_SIZE));
        String schema = SCHEMA_PATTERN.matcher(config.getString("db.url"))
                .results()
                .map(result -> result.group(1))
                .findFirst()
                .orElseThrow();
        dialect = SqlDialect.valueOf(schema.toUpperCase());
        setDriverClassName(dialect.getDriverClassName());
	}

}
