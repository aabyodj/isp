package by.aab.isp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    
    private static final String CONFIG_FILE_NAME = "/application.properties";
    
    private final Properties properties = new Properties();
    
    private Config() {
        try (InputStream in = getClass().getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(in);
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
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public String getString(String name) {
        return properties.getProperty(name);
    }
    
}
