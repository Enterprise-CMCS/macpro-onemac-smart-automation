package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AssignmentAndRouting extends BaseTest {

    @BeforeMethod
    public void setup() {
    //    createDriverSession();
    }

    @Test(groups = {"Assignment & Routing"})
    public void verifySpaCpocAssignmentWorkflow() throws InterruptedException {
        //    OSGUser osgUser = createNewOSGUser();
        //  spaPackage = osgUser.loginWithSharedSecret().goToSpasWaiversPage().clickNew().createSPARecord("CO", "Medicaid SPA");
        //  System.out.println(spaPackage.getPackageId());
        //   restartDriver();
       /* spaPackage = ExcelPackageSelector.selectSpa("CO", "Medicaid SPA", "");
        CPOCUser cpocUser = createNewCPOCUser();
        By cpocText = cpocUser.loginWithSharedSecret().goToSpasWaiversPage().
                openRecordFromAllRecordsView(spaPackage).
                assignToMe();
       TestAssert.assertTrue(getUtils().waitForTextToBePresent(cpocText, "CPOC4 AutomationCPOC"),"CPOC record name should display correctly after assignment to me");*/
        // TestAssert.assertEquals(getDriver(), cpocText, "CPOC4 AutomationCPOC", "CPOC record name should display correctly after assignment to me");
    }

    @Test(groups = {"Assignment & Routing"})
    public void testDashboard1() {
    }

    @Test(groups = {"Assignment & Routing"})
    public void testDashboard2() {
    }

    @Test(groups = {"Assignment & Routing"})
    public void testDashboard3()  {
    }
    @Test(groups = {"Assignment & Routing"})
    public void testDashboard4()  {
    }




   /* @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }*/
}
