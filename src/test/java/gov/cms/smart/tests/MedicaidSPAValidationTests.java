package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class MedicaidSPAValidationTests extends BaseTest {

    SpaPackage spaPackage;
    OSGUser osgUser;

    @BeforeClass
    public void setup() {
        createDriverSession();
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }

    /*@Test
    public void preparePriorityData() {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
    }*/

    @DataProvider
    public Object[][] priorityLevels() {
        return new Object[][]{
                {PriorityCode.ESCALATED_REVIEW, CodingAssessment.SAME},
                {PriorityCode.CUSTOMARY_REVIEW, CodingAssessment.UP_CODED},
                {PriorityCode.EXPEDITED_REVIEW, CodingAssessment.DOWN_CODED}
        };
    }

    @Test(dataProvider = "priorityLevels")
    public void verifyMedicaidSPAPriorityInfoWithDifferentPrioritiesAndCodingAssessment(PriorityCode priorityCode, CodingAssessment codingAssessment) throws InterruptedException {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        PriorityInfo actual = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).
                fillPriorityInfo(priorityCode, codingAssessment);
        PriorityInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPriorityInfo();
        TestAssert.assertEquals(actual, expected, "");
    }

    @Test()
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

    @Test()
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

    @Test
    public void testEmptyFormSubmission() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        TestAssert.assertTrue(osgUser.isInitialSubmissionDateErrorDisplayed(), "Date error should show.");
        TestAssert.assertTrue(osgUser.isStateErrorDisplayed(), "State error should show");
        TestAssert.assertTrue(osgUser.isIDNumberErrorDisplayed(), "ID Number error should show.");
    }

    @Test
    public void testInvalidDateFormat() throws InterruptedException {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        osgUser.enterInitialSubmissionDate("1234");
        boolean isDateFormateErrorDisplayed = osgUser.isDateFormatErrorDisplayed();
        TestAssert.assertTrue(isDateFormateErrorDisplayed, "Date format error should be displayed.");
    }

    /*@Test
    public void testValidSubmission() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("ID Number"), "ID Number should not have error");
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("Initial Submission Date"), "Date should not have error");
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("State"), "State should not have error");
        Assert.assertTrue(waiverForm.isFormSubmittedSuccessfully(), "Form should submit successfully");
    }*/

    /*@Test
    public void testEmptyIdNumber() {
        waiverForm.enterIdNumber("");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("ID Number"), "ID Number error expected");
    }*/

  /*  @Test
    public void testEmptyDate() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("Initial Submission Date"), "Date error expected");
    }*/

 /*   @Test
    public void testEmptyState() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("--None--");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("State"), "State error expected");
    }*/

    @Test
    public void testAuthorityFieldIsReadOnlyInNewSPAWaiversForm() throws InterruptedException {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By authority = By.xpath("//div[text()='This field is calculated upon save']");
        boolean elementVisible = getUtils().isElementVisible(authority);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void verifyHeaderModalTextIsCorrect() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By headerText = By.xpath("//h2[text()=\"New SPA or Waiver: Medicaid SPA\"]");
        boolean elementVisible = getUtils().isElementVisible(headerText);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");

    }

    @Test
    public void verifyIDNumberFieldIsPresent() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By IdNumber = By.xpath("//label[text()=\"ID Number\"]/following-sibling::div/input");
        boolean elementVisible = getUtils().isElementVisible(IdNumber);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");

    }

    @Test
    public void verifyInitialSubmissionDateFieldIsPresent() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By initialSubmissionDate = By.xpath("//label[text()=\"Initial Submission Date\"]/following-sibling::div/input");
        boolean elementVisible = getUtils().isElementVisible(initialSubmissionDate);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");

    }

    @Test
    public void verifyStateDropdownIsPresent() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By initialSubmissionDate = By.xpath("//label[text()=\"State\"]/parent::span/following-sibling::div[1]/lightning-base-combobox");
        boolean elementVisible = getUtils().isElementVisible(initialSubmissionDate);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");

    }

    @Test
    public void verifyPresenceOfCancelButton() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By cancelButton = By.xpath("//button[text()=\"Cancel\"]");
        boolean elementVisible = getUtils().isElementVisible(cancelButton);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void verifyPresenceOfSaveButton() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By save = By.xpath("//button[text()=\"Save\"]");
        boolean elementVisible = getUtils().isElementVisible(save);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void VerifyPresenceOfSaveAndNewButton() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By saveAndNew = By.xpath("//button[text()=\"Save & New\"]");
        boolean elementVisible = getUtils().isElementVisible(saveAndNew);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void VerifyThatTheAsteriskAppearsNextToInitialSubmissionDateField() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By date = By.xpath("//label[text()=\"Initial Submission Date\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(date);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }


    @Test
    public void VerifyThatTheAsteriskAppearsNextToStateDropdown() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By state = By.xpath("//label[text()=\"State\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(state);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void VerifyThatTheRequiredInformationTextIsDisplayed() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By infoText = By.xpath("//div[text()=\" = Required Information\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(infoText);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void VerifyThatIdentifyingInfoHeaderIsPresent() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By identifyingInfo = By.xpath("//span[text()=\"Identifying Information\"]/parent::h3");
        boolean elementVisible = getUtils().isElementVisible(identifyingInfo);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test
    public void verifyThatCancelButtonIsFunctional() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm().clickCancelButton();
        boolean isInvisible = getUtils().isElementInvisible(By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]"));
        TestAssert.assertTrue(isInvisible, "Modal should not be present");

    }

    @Test
    public void verifyThatCancelAndCloseButtonIsFunctional() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm().clickCancelAndClose();
        boolean isInvisible = getUtils().isElementInvisible(By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]"));
        TestAssert.assertTrue(isInvisible, "Modal should not be present");

    }

    @Test
    public void verifyThatAuthorityFieldIsReadOnlyInTheDetailsPage() {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        boolean isEditAuthorityButtonInvisible = getUtils().isElementInvisible(By.cssSelector("button[title=\"Edit Authority\"]"));
        TestAssert.assertTrue(isEditAuthorityButtonInvisible, "Edit Authority button should not be visible");

    }

    @Test
    public void verifyThatPriorityCodeFieldIsRequiredToSaveSPADetails() throws InterruptedException {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        getUtils().editByLabel("Priority Code");
        getUtils().selectFromComboBoxByLabel("Priority Code", "--None--");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        By priorityCodeCombobox = By.xpath(
                "//label[text()='Priority Code']/ancestor::lightning-combobox"
        );
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        // Wait until 'invalid' attribute appears
        wait.until(driver -> driver.findElement(priorityCodeCombobox).getAttribute("invalid") != null);

        // Assert that the attribute is present
        WebElement element = getDriver().findElement(priorityCodeCombobox);
        boolean isInvalid = element.getAttribute("invalid") != null;
        TestAssert.assertTrue(isInvalid, "Priority Code field should be invalid");
        PageFactory.getSpaDetailsPage(getDriver(),getUtils()).clickCancel();

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
