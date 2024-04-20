package domain;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigManager {
    private Properties properties;

    public ConfigManager(String configFilePath) {
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(configFilePath);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isCheckEnabled(String category, String checkName) {
        // Check if the entire category is enabled or not
        if (!isCategoryEnabled(category)) {
            // If category is not enabled, check if the specific check is explicitly enabled
            return Boolean.parseBoolean(properties.getProperty(category + "." + checkName, "false"));
        }
        // If category is enabled, return the specific check's setting or true if not specified
        return Boolean.parseBoolean(properties.getProperty(category + "." + checkName, "true"));
    }

    private boolean isCategoryEnabled(String category) {
        return Boolean.parseBoolean(properties.getProperty(category + ".enabled", "true"));
    }
}
