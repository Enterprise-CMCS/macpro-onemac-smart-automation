package gov.cms.smart.tests.functional.srt;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SRTTests extends BaseTest {

    @BeforeClass
    public void setup(){
        createDriverSession();
        srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
    }

    @Test(groups = {"User Access & Permissions"})
    public void verifyNonOSGOrCPOCUserCannotCreateMedicaidSPARecord() throws InterruptedException {
        srtUser.goToSPAWaiversPage();
        TestAssert.assertFalse(srtUser.isNewButtonPresent(), "");
    }


    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}