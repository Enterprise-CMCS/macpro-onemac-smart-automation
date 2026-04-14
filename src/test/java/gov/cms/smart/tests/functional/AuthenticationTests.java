package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthenticationTests extends BaseTest {

    @Test
    public void cpocAuthentication() {
        createDriverSession();
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret();
    }

    @Test
    public void osgAuthentication() {
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }

    @Test
    public void srtAuthentication() {
        createDriverSession();
        srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}
