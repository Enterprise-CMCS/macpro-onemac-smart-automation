package gov.cms.smart.base;


import gov.cms.smart.users.CPOCUser;
import gov.cms.smart.users.OSGUser;
import gov.cms.smart.users.SrtUser;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.config.TestContext;
import gov.cms.smart.utils.driver.DriverFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.annotations.AfterMethod;

@Getter
public class BaseTest implements ITest {

    public SpaPackage spaPackage;
    public OSGUser osgUser;
    public CPOCUser cpocUser;
    public SrtUser srtUser;
    // ThreadLocal driver for parallel safety
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    protected UIElementUtils utils;

    private static final ThreadLocal<String> testName = new ThreadLocal<>();

    protected void setTestName(Object... keyValues) {

        String methodName = Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();

        StringBuilder sb = new StringBuilder(methodName).append(" [");

        for (int i = 0; i < keyValues.length; i += 2) {
            if (i > 0) sb.append(" | ");

            Object key = keyValues[i];
            Object value = (i + 1 < keyValues.length) ? keyValues[i + 1] : "";

            sb.append(key).append("=").append(value);
        }

        sb.append("]");

        testName.set(sb.toString());
    }

    @Override
    public String getTestName() {
        return testName.get();
    }

    @AfterMethod(alwaysRun = true)
    public void clearTestName() {
        testName.remove();
    }

  /*  @BeforeMethod
    public void setUp() {
        createDriverSession();
    }*/

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
    protected void createDriverSession() {
        WebDriver webDriver = DriverFactory.createDriver(); // uses TestContext internally
        webDriver.get(TestContext.baseUrl());         // environment-aware URL
        //  webDriver.manage().window().maximize();
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

    protected CPOCUser createNewCPOCUser() {
        return new CPOCUser(getDriver(), getUtils());
    }

    protected SrtUser createNewSRTUser() {
        return new SrtUser(getDriver(), getUtils());
    }


}
