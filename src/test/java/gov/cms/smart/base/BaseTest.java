package gov.cms.smart.base;


import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.utils.DriverFactory;
import gov.cms.smart.utils.UIElementUtils;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

@Getter
public class BaseTest {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

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
      //  webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        webDriver.manage().window().maximize();
        driver.set(webDriver);
        utils = new UIElementUtils(getDriver(), 5);
    }

    public static WebDriver getDriver() {
        return driver.get();
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
