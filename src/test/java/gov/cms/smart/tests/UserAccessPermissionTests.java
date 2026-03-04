package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import org.testng.annotations.Test;

public class UserAccessPermissionTests extends BaseTest {

/*
    @BeforeMethod
    public void setup() {
        createDriverSession();
    }
*/

   /* @Test(testName = "UAP-001",
            groups = {"User Access & Permissions"},
            description = "Verify that OSG user can create a Medicaid SPA record when all mandatory fields are provided")
    public void verifyOSGUserCanCreateMedicaidSPARecord() throws InterruptedException {
     *//*   osgUser = createNewOSGUser();
        boolean isSPACreated = osgUser.loginWithSharedSecret().
                goToSpasWaiversPage().
                clickNew().
                createSPA("CO", "Medicaid SPA");
        TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");*//*
    }

    @Test(testName = "UAP-002",
            groups = {"User Access & Permissions"},
            description = "Verify that CPOC user can create a Medicaid SPA record when all mandatory fields are provided and record is saved successfully")
    public void verifyCPOCUserCanCreateMedicaidSPARecord() throws InterruptedException {
       *//* cpocUser = createNewCPOCUser();
        boolean isSPACreated = cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().
                clickNew().
                createSPA("CO", "Medicaid SPA");
        TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");*//*
    }

    @Test(testName = "UAP-003",
            groups = {"User Access & Permissions"},
            description = "Verify that users without OSG or CPOC role cannot create a Medicaid SPA record")
    public void verifyNonOSGOrCPOCUserCannotCreateMedicaidSPARecord() {*/
   /*     srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
        srtUser.goToSPAWaiversPage();
        Assert.assertFalse(srtUser.isNewButtonPresent());*/
    }



    /*@AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }*/
//}
