package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserAccessPermissionsTests extends BaseTest {

    @BeforeClass
    public void setup(){
        createDriverSession();
        srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
    }

    //User Access Permission Tests Start Here
      /* @Test(groups = {"User Access & Permissions"})
    public void verifyCPOCUserCanCreateMedicaidSPARecord() throws InterruptedException {
        boolean isSPACreated = PageFactory.
                getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().clickNew().
                createSPA("CO", "Medicaid SPA");
        TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }*/

    @Test(groups = {"User Access & Permissions"})
    public void verifyNonOSGOrCPOCUserCannotCreateMedicaidSPARecord() throws InterruptedException {
        srtUser.goToSPAWaiversPage();
        TestAssert.assertFalse(srtUser.isNewButtonPresent(), "");
    }   /* @Test(groups = {"User Access & Permissions"})
    public void verifyOSGUserCanCreateMedicaidSPARecord() throws InterruptedException {
        boolean isSPACreated = PageFactory.getHomePage(getDriver(), getUtils()).
                goToSpasWaiversPage().
                clickNew().
                createSPA("CO", "Medicaid SPA");
        TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }*/

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
