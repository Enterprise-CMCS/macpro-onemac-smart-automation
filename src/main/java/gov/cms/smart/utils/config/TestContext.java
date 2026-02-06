package gov.cms.smart.utils.config;

public final class TestContext {

    private static final String ENV;

    static {
        ENV = ConfigReader.get("runOn");
        if (ENV == null || ENV.isBlank()) {
            throw new RuntimeException("runOn not set");
        }
    }

    private TestContext() {}

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

    public static String osgSharedSecret() {
        return ConfigReader.get("osg." + env() + ".secret");

    }

    public static String password() {
        return ConfigReader.get("password");
    }

    // ---------- FILES ----------
    public static String packagesFile() {
        return ConfigReader.get("packages." + env());
    }

    // ---------- BROWSER ----------
    public static String browser() {
        return ConfigReader.get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(ConfigReader.get("headless"));
    }
}
