package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.flows.CPOCUser;
import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.flows.SRTUser;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MedicaidSPAFlowsTest extends BaseTest {

    @BeforeClass()
    public void setup() {
        createDriverSession();
    }


    //SPA Flows
    @Test
    public void verifySpaCpocAssignmentWorkflow() throws InterruptedException {
     /*   OSGUser osgUser = createNewOSGUser();
        SpaPackage spaPackage = osgUser.loginWithSharedSecret()
                .goToSpasWaiversPage().
                openExistingRecord("CO", "Medicaid SPA");
        restartDriver();
        CPOCUser cpocUser = createNewCPOCUser();
        By actual = cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().
                openRecordFromAllRecordsView(spaPackage).assignToMe();
        TestAssert.assertEquals(getDriver(), actual, "CPOC4 AutomationCPOC", "CPOC record name should display correctly after assignment to me");*/
    }

    @Test
    public void verifySpaSRTAssignmentWorkflow() throws InterruptedException {
        CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().assignToMe();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).
                openRecordFromAllRecordsView(ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", "")).
                assignSRT("Automation", "This is an automated test script for SRT assignment from CPOC");

    }


    @Test
    public void verifySRTReviewRecommendedApprovalWorkflow() throws InterruptedException {
       /* SRTUser srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret().goToSpasWaiversPage().openExistingRecord("CO", "Medicaid SPA");
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).fillSRTDetails("Recommend Approval");*/


    }

    @Test
    public void verifyCPOCReviewRequestRAI() {
       /* CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().goToSpasWaiversPage().openExistingRecord("AL", "Medicaid SPA");
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).requestRAI();*/
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
