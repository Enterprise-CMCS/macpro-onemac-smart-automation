package gov.cms.smart.base;


import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.utils.DriverFactory;
import gov.cms.smart.utils.UIElementUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private UIElementUtils utils;

    @BeforeMethod
    public void setUp() {
        createDriverSession();
    }

    protected void restartDriver() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception ignored) {
            }
        }
        createDriverSession();
    }

    private void createDriverSession() {
        WebDriver webDriver = DriverFactory.createDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        webDriver.manage().window().maximize();
        driver.set(webDriver);

        utils = new UIElementUtils(getDriver(), 10);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public UIElementUtils getUtils() {
        return utils;
    }


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
