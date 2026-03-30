package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static gov.cms.smart.pages.SPAsWaiversPage.NEW_BUTTON;

public class UserAccessPermissionsTests extends BaseTest {

    @BeforeMethod
    public void setup() {
        createDriverSession();
    }

    //User Access Permission Tests Start Here
    @Test(groups = {"User Access & Permissions", "CPOC"})
    public void verifyThatNewButtonIsDisplayedForCPOC() {
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret();
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
        TestAssert.assertTrue(utils.isVisible(NEW_BUTTON), "");
        //   createSPA("CO", "Medicaid SPA");
        //   TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }

    @Test(groups = {"User Access & Permissions"})
    public void verifyThatNewButtonIsNotDisplayedForSRT() throws InterruptedException {
        srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
        srtUser.goToSPAWaiversPage();
        TestAssert.assertFalse(srtUser.isNewButtonPresent(), "");
    }

    @Test(groups = {"User Access & Permissions"})
    public void verifyThatNewButtonIsDisplayedOSG() throws InterruptedException {
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
        osgUser.goToSPAWaiversPage();
        //  TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }

    @Test(groups = {"User Access & Permissions"})
    public void userAccessPermission1(){
        //  TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }
    @Test(groups = {"User Access & Permissions"})
    public void userAccessPermission2(){
        //  TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }
    @Test(groups = {"User Access & Permissions"})
    public void userAccessPermission3(){
        //  TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }

    @Test(groups = {"User Access & Permissions"})
    public void userAccessPermission4(){
        //  TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }

    @AfterMethod
    public void rebootDriver(){
        restartDriver();
    }

    //User Access Permission Tests End Here
    @AfterClass(alwaysRun = true)

    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }
}
