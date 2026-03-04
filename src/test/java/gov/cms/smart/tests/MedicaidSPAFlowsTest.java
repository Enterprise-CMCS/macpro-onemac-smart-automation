package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;

public class MedicaidSPAFlowsTest extends BaseTest {

   /* @BeforeClass()
    public void setup() {
        createDriverSession();
    }*/



  /*  //SPA Flows
    @Test
    public void verifySpaCpocAssignmentWorkflow() throws InterruptedException {
        OSGUser osgUser = createNewOSGUser();
        SpaPackage spa = osgUser.
                loginWithSharedSecret().
                goToSpasWaiversPage().
                clickNew().
                createSPA("MD", "Medicaid SPA");
        restartDriver();
        Thread.sleep(5000);
        CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.
                loginWithSharedSecret().
                goToSpasWaiversPage().
                openRecordFromAllRecordsView(spa).
                assignRecord();
        //  By actual =
       *//*  cpocUser.login().
                goToSpasWaiversPage().
                openExistingRecord(spa);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).assignToMe();*//*
        //PageFactory.getSpaWaiversPage(getDriver(),getUtils()).assignSRT("SRT4 AutomationSRT","this is an automated test script to test cpoc");
        //TestAssert.assertEquals(getDriver(), actual, "CPOC4 AutomationCPOC", "CPOC record name should display correctly after assignment to me");
    }*/

 /*   @Test
    public void test(){
        CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret();
    }*/
/*



  /*  @Test
    public void verifySRTReviewRecommendedApprovalWorkflow() throws InterruptedException {
        // SRTUser srtUser = createNewSRTUser();
        // srtUser.loginWithSharedSecret().goToSpasWaiversPage().openExistingRecord("CO", "Medicaid SPA");
        //  PageFactory.getSpaWaiversPage(getDriver(), getUtils()).fillSRTDetails("Recommend Approval");

    }*/
/*
    @Test
    public void verifyCPOCReviewRequestRAI() {
        *//* CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().goToSpasWaiversPage().openExistingRecord("AL", "Medicaid SPA");
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).requestRAI();*//*
    }*/

 /*   @Test
    public void verifySRTReviewMixedDecisions() throws InterruptedException, IOException {
        OSGUser osgUser = createNewOSGUser();
        osgUser.login();
        CPOCUser cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().openExistingRecord(ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", ""));
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).assignSRT("SRT Test Automation User", "This is an automated test script to validate SRT Test Automation User assignment");
        SRTUser srtUser = createNewSRTUser();
        srtUser.loginAsSRT2();
        //  By salesforceMenu = By.xpath("//one-app-launcher-header/..");
        //  getUtils().clickElement(salesforceMenu);
        //   By smartApp = By.xpath("//a[@data-label=\"SMART\"]/..");
        //  getUtils().clickElement(smartApp);
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).openRecordFromAllRecordsView((ExcelPackageSelector.selectSpa("NY", "Medicaid SPA", "")));
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).fillSRT2DetailsRequestRAI("In Progress");
    }*/


/*    @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }*/

}
