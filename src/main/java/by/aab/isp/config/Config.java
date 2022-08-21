package by.aab.isp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Config {
    
    private static final String CONFIG_FILE_NAME = "/application.properties";
    private static final Map<String, String> ENV_VARIABLE_NAMES = Map.of(
            "ISP_DB_URL", "db.url",
            "ISP_DB_USER", "db.user",
            "ISP_DB_PASSWORD", "db.password",
            "ISP_DB_POOLSIZE", "db.poolsize",
            "ISP_HOMEPAGE_PROMOTIONS_COUNT", "homepage.promotionsCount"
    );
    
    private final Properties properties = new Properties();
    
    private Config() {
        try (InputStream in = getClass().getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(in);
            ENV_VARIABLE_NAMES.forEach((env, prop) -> {
                String value = System.getenv(env);
                if (value != null) {
                    properties.setProperty(prop, value);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static class BillPughSingleton {
        static final Config INSTANCE = new Config();
    }
    
    public static Config getInstance() {
        return BillPughSingleton.INSTANCE;
    }

    public int getInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(name));
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }
    
    public String getString(String name) {
        return properties.getProperty(name);
    }
    
}
