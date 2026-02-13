package gov.cms.smart.utils.config;

public final class TestContext {

    private static final String ENV;

    static {
        ENV = ConfigReader.get("runOn");
        if (ENV == null || ENV.isBlank()) {
            throw new RuntimeException("runOn not set");
        }
    }

    private TestContext() {
    }

    public static String env() {
        return ENV;
    }

    // ---------- URL ----------
    public static String baseUrl() {
        return ConfigReader.get("baseUrl." + env());
    }

    // ---------- USERS ----------
    public static String osgUsername() {
        return ConfigReader.get("osg." + env() + ".username");
    }

    public static String srtUsername() {
        return ConfigReader.get("srt." + env() + ".username");
    }

    public static String osgSharedSecret() {
        return ConfigReader.get("osg." + env() + ".secret");

    }

    public static String srtSharedSecret() {
        return ConfigReader.get("srt." + env() + ".secret");

    }

    public static String cpocUsername() {
        return ConfigReader.get("cpoc." + env() + ".username");
    }

    public static String cpocSharedSecret() {
        return ConfigReader.get("cpoc." + env() + ".secret");

    }

    public static String password() {
        return ConfigReader.get("password");
    }

    // ---------- FILES ----------
    public static String packagesFile() {
        return ConfigReader.get("packages." + env());
    }

    public static String getStateCountersFile() {
        return ConfigReader.get("stateCounters." + env());
    }

    // ---------- BROWSER ----------
    public static String browser() {
        return ConfigReader.get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(ConfigReader.get("headless"));
    }
}
