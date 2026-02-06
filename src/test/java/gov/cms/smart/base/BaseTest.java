package gov.cms.smart.base;


import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.utils.config.ConfigReader;
import gov.cms.smart.utils.config.TestContext;
import gov.cms.smart.utils.driver.DriverFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@Getter
public class BaseTest {

    // ThreadLocal driver for parallel safety
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected UIElementUtils utils;

    @BeforeMethod
    public void setUp() {
        createDriverSession();
    }

    /**
     * Restart the WebDriver in the same thread
     */
    protected void restartDriver() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception ignored) {
            }
        }
        createDriverSession();
    }

    /**
     * Create a new WebDriver session
     */
    private void createDriverSession() {
        WebDriver webDriver = DriverFactory.createDriver(); // uses TestContext internally
        webDriver.get(TestContext.baseUrl());         // environment-aware URL
        webDriver.manage().window().maximize();
        driver.set(webDriver);
        utils = new UIElementUtils(getDriver(), 10);
    }

    /**
     * Access the thread-safe WebDriver
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Access UIElementUtils
     */
    protected UIElementUtils getUtils() {
        return utils;
    }

    /**
     * Helper to create an OSGUser page object
     */
    protected OSGUser createNewOSGUser() {
        return new OSGUser(getDriver(), getUtils());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = driver.get();
        if (d != null) {
            d.quit();
            driver.remove();
        }
    }
}
