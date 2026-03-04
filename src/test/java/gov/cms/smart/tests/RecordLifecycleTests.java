package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class RecordLifecycleTests extends BaseTest {

   /* @BeforeClass
    public void setup() {
        createDriverSession();
        osgUser = createNewOSGUser();
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        osgUser.loginWithSharedSecret();
    }

    @DataProvider
    public Object[][] priorityLevels() {
        return new Object[][]{
                {PriorityCode.ESCALATED_REVIEW, CodingAssessment.SAME},
                {PriorityCode.CUSTOMARY_REVIEW, CodingAssessment.UP_CODED},
                {PriorityCode.EXPEDITED_REVIEW, CodingAssessment.DOWN_CODED}
        };
    }

    @Test(dataProvider = "priorityLevels", groups = {"Record Lifecycle Management"})
    public void verifyMedicaidSPAPriorityInfoWithDifferentPrioritiesAndCodingAssessment(PriorityCode priorityCode, CodingAssessment codingAssessment) throws InterruptedException {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        PriorityInfo actual = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).
                fillPriorityInfo(priorityCode, codingAssessment);
        PriorityInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPriorityInfo();
        TestAssert.assertEquals(actual, expected, "");
    }

    @Test(groups = {"Record Lifecycle Management"})
    public void verifyThatCancelButtonIsFunctional() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm().clickCancelButton();
        boolean isInvisible = getUtils().isElementInvisible(By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]"));
        TestAssert.assertTrue(isInvisible, "Modal should not be present");
    }

    @Test(groups = {"Record Lifecycle Management"})
    public void verifyThatCancelAndCloseButtonIsFunctional() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm().clickCancelAndClose();
        boolean isInvisible = getUtils().isElementInvisible(By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]"));
        TestAssert.assertTrue(isInvisible, "Modal should not be present");

    }

    @Test(groups = {"Record Lifecycle Management"})
    public void verifyindentifyinginfomedicaidspa() {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        IdentifyingInfo actual = new IdentifyingInfo();
        actual.setAuthority(spaPackage.getAuthority());
        actual.setIdNumber(spaPackage.getPackageId());
        String state = getUtils().getStateFullName(spaPackage.getState());
        actual.setState(state);
        IdentifyingInfo expected = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).readIdentifyingInfo();
        TestAssert.assertEquals(actual, expected, "Should save identifying information.");
    }


    @Test(groups = {"Record Lifecycle Management"})
    public void verifyPriorityInformationSectionIsVisible() throws InterruptedException {
        PageFactory.getHomePage(getDriver(), getUtils()).
                goToSpasWaiversPage().
                openRecordFromAllRecords(spaPackage);
        boolean isPrioritySectionVisible = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).isPriorityInfoPresent();
        TestAssert.assertTrue(
                isPrioritySectionVisible,
                "Priority Information section is not visible on Medicaid SPA details page"
        );
    }

    @Test(groups = {"Record Lifecycle Management"})
    public void verifyPlanInformation() {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        PlanInfo actual = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils())
                .fillPlanInfo("Test Subject", "Test Description");
        PlanInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPlanInfo();
        TestAssert.assertEquals(actual, expected, "Should save plan information");
    }


    @Test(groups = {"Record Lifecycle Management"})
    public void verifyPriorityFieldsAreGroupedUnderPriorityInformationSection() throws InterruptedException {
        PageFactory.getHomePage(getDriver(), getUtils()).
                goToSpasWaiversPage().
                openRecordFromAllRecords(spaPackage);
        boolean areFieldsGroupedCorrectly = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).areFieldsGroupedCorrectly();
        TestAssert.assertTrue(
                areFieldsGroupedCorrectly,
                "Priority fields are not properly grouped within the Priority Information section"
        );
    }


    @AfterMethod
    public void goBackToHomePage() {
        PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                goToHomePage();
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
