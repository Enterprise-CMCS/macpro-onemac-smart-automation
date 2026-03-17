package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.dataproviders.DataProviders;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

import static gov.cms.smart.dataproviders.DataProviders.GROUP_TO_DIVISIONS;

public class AssignmentAndRoutingTests extends BaseTest {

    @BeforeClass
    public void setup() {
        createDriverSession();
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret();
    }
    @BeforeGroups(groups = "groupDivisionValidation")
    public void NavigateToReviewTab() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().openExistingRecord(spaPackage).goToReviewTab();
    }
    @Test(groups = {"Assignment & Routing", "groupDivisionValidation"}, dataProvider = "groupDivisionData", dataProviderClass = DataProviders.class)
    public void verifyDivisionListFiltersCorrectlyForEachGroup(String group) throws InterruptedException {
        setTestName("group", group);
        utils.selectFromComboBoxByLabel("Group", group);
        List<String> actualOptions =
                utils.getValuesFromDropdownByLabel("Division");
        List<String> expectedOptions =
                GROUP_TO_DIVISIONS.get(group);
        List<String> missing =
                expectedOptions.stream()
                        .filter(e -> !actualOptions.contains(e))
                        .collect(Collectors.toList());
        List<String> extra =
                actualOptions.stream()
                        .filter(a -> !expectedOptions.contains(a))
                        .collect(Collectors.toList());
        TestAssert.assertTrue(
                missing.isEmpty() && extra.isEmpty(),
                "Division mismatch for group: " + group +
                        "\nMissing: " + missing +
                        "\nExtra: " + extra
        );
    }

    @AfterGroups(groups = "groupDivisionValidation")
    public void navigateBack() {
        PageFactory.getHomePage(getDriver(), getUtils());
    }
  /*  @Test(groups = {"Assignment & Routing"})
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

    }*/

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



    @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }
}
