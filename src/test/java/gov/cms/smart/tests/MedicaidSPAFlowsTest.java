package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.flows.CPOCUser;
import gov.cms.smart.flows.SRTUser;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class MedicaidSPAFlowsTest extends BaseTest {

/*    @BeforeClass()
    public void setup() {
        createDriverSession();
    }


    //SPA Flows
    @Test
    public void verifySpaCpocAssignmentWorkflow() throws InterruptedException {
   *//*     OSGUser osgUser = createNewOSGUser();
        SpaPackage spaPackage = osgUser.login()
                .goToSpasWaiversPage().openExistingRecord(s);
        restartDriver();*//*
        CPOCUser cpocUser = createNewCPOCUser();
        //  By actual =
        cpocUser.login().
                goToSpasWaiversPage().
                openRecordFromAllRecordsView(ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", ""));
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).assignToMe();
        //PageFactory.getSpaWaiversPage(getDriver(),getUtils()).assignSRT("SRT4 AutomationSRT","this is an automated test script to test cpoc");
        //   TestAssert.assertEquals(getDriver(), actual, "CPOC4 AutomationCPOC", "CPOC record name should display correctly after assignment to me");
    }

    @Test
    public void verifySpaSRTAssignmentWorkflow() {
        CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.login().goToSpasWaiversPage().openExistingRecord(ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", ""));
              PageFactory.getSpaWaiversPage(getDriver(),getUtils()).assignSRT("SRT Test Automation User", "Test assignment");
        *//* goToSpasWaiversPage().assignToMe();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).
                openRecordFromAllRecordsView(ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", "")).
                assignSRT("Automation", "This is an automated test script for SRT assignment from CPOC");*//*

    }


    @Test
    public void verifySRTReviewRecommendedApprovalWorkflow() throws InterruptedException {
       *//* SRTUser srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret().goToSpasWaiversPage().openExistingRecord("CO", "Medicaid SPA");
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).fillSRTDetails("Recommend Approval");*//*

    }

    @Test
    public void verifyCPOCReviewRequestRAI() {
       *//* CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().goToSpasWaiversPage().openExistingRecord("AL", "Medicaid SPA");
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).requestRAI();*//*
    }

    @Test
    public void verifySRTReviewMixedDecisions() throws InterruptedException, IOException {
       *//* OSGUser osgUser = createNewOSGUser();
        osgUser.login();*//*
       *//* CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().openExistingRecord(ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", ""));
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).assignSRT("SRT Test Automation User", "This is an automated test script to validate SRT Test Automation User assignment");*//*
        SRTUser srtUser = createNewSRTUser();
        srtUser.loginAsSRT2();
        //  By salesforceMenu = By.xpath("//one-app-launcher-header/..");
        //  getUtils().clickElement(salesforceMenu);
        //   By smartApp = By.xpath("//a[@data-label=\"SMART\"]/..");
        //  getUtils().clickElement(smartApp);
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).openRecordFromAllRecordsView((ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", "")));
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).fillSRT2DetailsRequestRAI("In Progress");
    }


    @Test
    public void cpoc() {
        CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.login();
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }*/
}
