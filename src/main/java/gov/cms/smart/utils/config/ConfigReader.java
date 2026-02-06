package gov.cms.smart.utils.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config.properties", e);
            }
        }
        return properties;
    }

    /**
     * Reads a config value with ENV override.
     * If ENV var exists → use it
     * Otherwise → use config.properties
     */
    public static String get(String key) {
        // 1. Check environment variables (preferred)
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // 2. Fallback to config.properties
        return getProperties().getProperty(key);
    }

    /**
     * Retrieve username for user type (cms, state, seatool…)
     * ENV example: CMS_USERNAME, STATE_USERNAME
     * config.properties example: cms.username
     */

    public static String getUserName(String userRole, String env) {
        return get(userRole + "." + env + ".username");
    }

    public static String getPassword() {
        return get("password");
    }

    /**
     * Retrieve password for user type
     * ENV example: CMS_PASSWORD
     * config.properties example: cms.password
     */

}
