package gov.cms.smart.tests.functional.osg;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.dataproviders.DataProviders;
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
import org.testng.Assert;
import org.testng.annotations.*;

import static gov.cms.smart.pages.AdjudicationTab.DOCUMENTS_POSTED_TO_MEDICAID_GOV_LABEL;


public class RecordLifecycleManagementTests extends BaseTest {

    @BeforeClass()
    public void setup() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }

    @BeforeGroups(groups = "RecordManagementDetailsPage")
    public void searchSPA() throws InterruptedException {
        osgUser.goToSPAWaiversPage().searchSPA(spaPackage);
    }

    @BeforeMethod(onlyForGroups = "RecordManagementDetailsPage")
    public void openSPA() {
        utils.openRecord(spaPackage.getPackageId());
    }

    @Test(dataProvider = "priorityLevels", groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"}, dataProviderClass = DataProviders.class)
    public void verifyMedicaidSPAPriorityInfoWithDifferentPrioritiesAndCodingAssessment(PriorityCode priorityCode, CodingAssessment codingAssessment) throws InterruptedException {
        setTestName("priorityCode", priorityCode,"codingAssessment",codingAssessment);
        PriorityInfo actual = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).
                fillPriorityInfo(priorityCode, codingAssessment);
        PriorityInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPriorityInfo();
        TestAssert.assertEquals(actual, expected, "Priority Info should persist correctly after save");
    }

    @AfterMethod(onlyForGroups = "RecordManagementDetailsPage")
    public void navigateToRecentlyViewed() throws InterruptedException {
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
    }

    @Test(groups = {"Record Lifecycle Management"})
    public void verifyThatCancelButtonIsFunctional() throws InterruptedException {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm().clickCancelButton();
        boolean isInvisible = getUtils().isElementInvisible(By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]"));
        TestAssert.assertTrue(isInvisible, "Modal should not be present");
    }

    @Test(groups = {"Record Lifecycle Management"})
    public void verifyThatCancelAndCloseButtonIsFunctional() throws InterruptedException {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm().clickCancelAndClose();
        boolean isInvisible = getUtils().isElementInvisible(By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]"));
        TestAssert.assertTrue(isInvisible, "Modal should not be present");

    }

    @Test(groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"})
    public void verifyidentifyinginfomedicaidspa() {
        IdentifyingInfo actual = new IdentifyingInfo();
        actual.setAuthority(spaPackage.getAuthority());
        actual.setIdNumber(spaPackage.getPackageId());
        String state = getUtils().getStateFullName(spaPackage.getState());
        actual.setState(state);
        IdentifyingInfo expected = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).readIdentifyingInfo();
        TestAssert.assertEquals(actual, expected, "Should save identifying information.");
    }


    @Test(groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"})
    public void verifyPriorityInformationSectionIsVisible() {
        boolean isPrioritySectionVisible = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).isPriorityInfoPresent();
        TestAssert.assertTrue(
                isPrioritySectionVisible,
                "Priority Information section is not visible on Medicaid SPA details page"
        );
    }

    @Test(groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"})
    public void verifyPlanInformation() {
        PlanInfo actual = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils())
                .fillPlanInfo("Test Subject", "Test Description");
        PlanInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPlanInfo();
        TestAssert.assertEquals(actual, expected, "Should save plan information");
    }


    @Test(groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"})
    public void verifyPriorityFieldsAreGroupedUnderPriorityInformationSection() {
        boolean areFieldsGroupedCorrectly = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).areFieldsGroupedCorrectly();
        TestAssert.assertTrue(
                areFieldsGroupedCorrectly,
                "Priority fields are not properly grouped within the Priority Information section"
        );
    }

    @Test(groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"})
    public void verifySubmissionVerifiedCompleteFieldAutoPopulates() throws InterruptedException {
        utils.editByLabel("Priority Code");
        utils.selectFromComboBoxByLabel("Initial Submission Complete", "Yes");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        utils.waitForFieldTextToBe("Submission Information", "Submission Verified Complete", utils.getTodayDateFormatted());
        Assert.assertEquals(utils.getFieldTextByLabel("Submission Verified Complete"), utils.getTodayDateFormatted());
    }

    @Test(groups = {"Record Lifecycle Management", "RecordManagementDetailsPage"})
    public void verifyDocumentsPostedToMedicaidGovLabelDisplayedInsteadOfApprovalDocsReceived() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToAdjudicationTab();
        TestAssert.assertTrue(
                utils.isElementVisible(DOCUMENTS_POSTED_TO_MEDICAID_GOV_LABEL),
                "Label 'Documents posted to Medicaid.gov' should be displayed."
        );

    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}
