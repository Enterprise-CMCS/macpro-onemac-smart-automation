package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SRTReviewTests extends BaseTest {

    @BeforeClass()
    public void setup() throws InterruptedException {
        spaPackage = ExcelPackageSelector.selectSpa("CO", "Medicaid SPA", "");
        createDriverSession();
        srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().searchSPA(spaPackage);
        utils.openRecord(spaPackage.getPackageId());
    }

    /*
        @Test(groups = {"RAI Management", "Prepare RAI Questions"})
        public void verifyRAICreationWhenRAINeededAndQuestionsCompleted() throws InterruptedException {
            srtUser = createNewSRTUser();
            srtUser.loginWithSharedSecret();
            PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().searchSPA(spaPackage);
            utils.openRecord(spaPackage.getPackageId());
            PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToReviewTab();
            utils.scrollToElement(RAI_INFO_SECTION);
            utils.clickElement(SRT_QUESTIONS_ARROW);
            utils.clickElement(EDIT_RAI_QUESTION);
            utils.waitForVisibility(By.tagName("lightning-primitive-bubble"));
            utils.selectFromComboBoxByLabel("RAI Needed", "Yes");
            utils.sendKeysToTextAreaByLabel("RAI Questions", "Needs More Information for further processing");
            utils.selectFromComboBoxByLabel("RAI Questions Completed", "Yes");
            utils.clickElement(SAVE_BUTTON);
            utils.waitForVisibility(RAI_QUESTIONS_SAVED);
        }
    */
    @Test(groups = {"SRT Review"})
    public void verifyThatPriorityFieldIsReadOnlyForSRTUser() {
        By priorityCode = By.xpath("//span[text()=\"Priority Code\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(priorityCode), "SRT should not be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verifyThatApprovedEffectiveDateFieldIsReadOnlyForSRTUser() {
        By approvedEffectiveDate = By.xpath("//span[text()=\"Approved Effective Date\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(approvedEffectiveDate), "SRT should not be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verifyThatCodingAfterInitialAssessmentFieldIsReadOnlyForSRTUser() {
        By codingAfterInitialAssessment = By.xpath("//span[text()=\"Coding After Initial Assessment\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(codingAfterInitialAssessment), "SRT should not be able to edit field");
    }
    @Test(groups = {"SRT Review"})
    public void verifyThatInitialSubmissionCompleteFieldIsReadOnlyForSRTUser() {
        By initialSubmissionComplete = By.xpath("//span[text()=\"Initial Submission Complete\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(initialSubmissionComplete), "SRT should not be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verifyThatDateOfCodingFieldIsReadOnlyForSRTUser() {
        By dateOfCodingChange = By.xpath("//span[text()=\"Date Of Coding Change\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(dateOfCodingChange), "SRT should not be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verifyThatDescriptionFieldIsReadOnlyForSRTUser() {
        By description = By.xpath("//span[text()=\"Description\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(description), "SRT should not be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verifyPriorityCommentsMemoFieldIsEditableForSRTUser() {
        By priorityCommentsMemo = By.xpath("//span[text()=\"Priority Comments Memo\"]/../following-sibling::div//button");
        TestAssert.assertTrue(utils.isVisible(priorityCommentsMemo), "SRT should be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verifyMissingInformationFieldIsReadOnlyForSRTUser() {
        By missingInformation = By.xpath("//span[text()=\"Missing Information\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(missingInformation), "SRT should not be able to edit field");
    }

    @Test(groups = {"SRT Review"})
    public void verify15thDayCallHeldFieldIsReadOnlyForSRTUser() {
        By fifteenDayCallHeld = By.xpath("//span[text()=\"15th Day Call Held\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(fifteenDayCallHeld), "SRT should not be able to edit field");
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}
