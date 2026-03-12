package gov.cms.smart.tests.flows;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AssignmentAndRoutingTests extends BaseTest {

    @BeforeMethod
    public void setup() {
        createDriverSession();
    }

    @Test(groups = {"Assignment & Routing"})
    public void verifySpaCpocAssignmentWorkflow() throws InterruptedException {
        osgUser = createNewOSGUser();
        spaPackage = osgUser.
                loginWithSharedSecret().
                goToSpasWaiversPage().
                clickNew().
                createSPARecord("CO", "Medicaid SPA");
        restartDriver();
        cpocUser = createNewCPOCUser();
        boolean isAssigned = cpocUser.
                loginWithSharedSecret().
                goToSpasWaiversPage().
                cpocOpenRecordFromAll(spaPackage).
                assignToMe(spaPackage);
        TestAssert.assertTrue(isAssigned, "CPOC record name should display correctly after assignment to me");

    }

  /*  @Test
    public void VerifyCPOCusercanreviewthesubmissiondetailsfromSRTAndCreateRAI() throws InterruptedException {
        spaPackage = new SpaPackage();
        spaPackage.setPackageId("CO-25-9063");
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().openRecordFromAllRecords(spaPackage).goToReviewTab().assignSRT();
                  clickNew().
                createSPARecord("CO", "Medicaid SPA");
        PageFactory.
                getSpaWaiversPage(getDriver(), getUtils()).
                cpocOpenRecordFromAll(spaPackage);

        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToReviewTab();
        srtUser = createNewSRTUser();

        srtUser.login();
        srtUser.goToSPAWaiversPage();
        srtUser.openRecord(spaPackage);


    }*/

    /*@Test(groups = {"Assignment & Routing"})
    public void verifySpaSRTAssignmentWorkflow() throws InterruptedException {
        CPOCUser cpocUser = createNewCPOCUser();
        spaPackage = new SpaPackage();
        spaPackage.setPackageId("CO-25-9063");
        cpocUser.loginWithSharedSecret().goToSpasWaiversPage().openRecordFromAllRecords(spaPackage);
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToReviewTab().assignSRT();
        // PageFactory.getSpaWaiversPage(getDriver(), getUtils()).assignSRT("SRT Test Automation User", "Test assignment");
        goToSpasWaiversPage().assignToMe();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).
                openRecordFromAllRecordsView(ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", "")).
                assignSRT("Automation", "This is an automated verifyDateOnActiveClockUpdateAutomatically script for SRT assignment from CPOC");

    }*/

    @Test()
    public void testDashboard1() {
    }

    @Test(groups = {"Assignment & Routing"})
    public void testDashboard2() {
    }

    @Test(groups = {"Assignment & Routing"})
    public void testDashboard3() {
    }

    @Test(groups = {"Assignment & Routing"})
    public void testDashboard4() {
    }




    @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }
}
