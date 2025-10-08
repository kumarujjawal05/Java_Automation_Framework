package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the properties file!", e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Key '" + key + "' not found in config.properties file!");
        }
        return value;
    }
}
