package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static gov.cms.smart.pages.SpaDetailsPage.MILESTONES_SECTION;
import static gov.cms.smart.pages.SpaDetailsPage.SUB_TYPE_SI;

public class ValidationsAndBusinessRules extends BaseTest {

   /* @BeforeClass
    public void setup() {
        createDriverSession();
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }*/


    @Test(groups = {"Validations & Business Rules"})
    public void verifySubtypeLabelFormattingIsCorrect() {
      /*  PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().openExistingRecord(spaPackage);
        getUtils().scrollToElement(MILESTONES_SECTION);
        TestAssert.assertEquals(getDriver(), SUB_TYPE_SI, "Subtype", "Subtype label text or formatting is incorrect");*/
    }


   /* @Test(groups = {})
    public void testSkip() {
        throw new SkipException("BLOCKED: Feature disabled / data not available / env issue");
    }*/

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatPaymentTypeHasCorrectSubtypes() throws InterruptedException {
/*        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        boolean arePaymentOptionsValid = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).validateSubtypes("Type", "Payment", "Subtype");
        TestAssert.assertTrue(arePaymentOptionsValid, "Payment type subtypes are not correct");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }


    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatEligibilityTypeHasCorrectSubtypes() throws InterruptedException {
     /*   osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        boolean arePaymentOptionsValid = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).validateSubtypes("Type", "Eligibility", "Subtype");
        TestAssert.assertTrue(arePaymentOptionsValid, "Eligibility type subtypes are not correct");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatBenefitsTypeHasCorrectSubtypes() throws InterruptedException {
   /*     osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        boolean arePaymentOptionsValid = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).validateSubtypes("Type", "Benefits", "Subtype");
        TestAssert.assertTrue(arePaymentOptionsValid, "Eligibility type subtypes are not correct");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatSubtypeIsDisabledWhenTypeIsAdmin() throws InterruptedException {
/*        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        getUtils().editByLabel("Priority Code");
        getUtils().selectFromComboBoxByLabel("Type", "Admin");
        By subType = By.xpath(
                "//label[text()=\"Subtype\"]/../following-sibling::div/lightning-base-combobox"
        );
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        // Wait until 'invalid' attribute appears
        wait.until(driver -> driver.findElement(subType).getAttribute("disabled") != null);
        // Assert that the attribute is present
        WebElement element = getDriver().findElement(subType);
        boolean isDisabled = element.getAttribute("disabled") != null;
        TestAssert.assertTrue(isDisabled, "combo box should be disabled");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/


    }

    @Test(groups = {"Validations & Business Rules"})
    public void testEmptyFormSubmission() {
     /*   osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        try {
            TestAssert.assertTrue(
                    osgUser.isInitialSubmissionDateErrorDisplayed(),
                    "Date error should show."
            );

            TestAssert.assertTrue(
                    osgUser.isStateErrorDisplayed(),
                    "State error should show"
            );

            TestAssert.assertTrue(
                    osgUser.isIDNumberErrorDisplayed(),
                    "ID Number error should show."
            );

        } finally {
            PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        }*/
    }


    @Test(groups = {"Validations & Business Rules"})
    public void testInvalidDateFormat() throws InterruptedException {
    /*    osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        osgUser.enterInitialSubmissionDate("1234");
        boolean isDateFormateErrorDisplayed = osgUser.isDateFormatErrorDisplayed();
        TestAssert.assertTrue(isDateFormateErrorDisplayed, "Date format error should be displayed.");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }


    @Test(groups = {"Validations & Business Rules"})
    public void testAuthorityFieldIsReadOnlyInNewSPAWaiversForm() {
/*        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By authority = By.xpath("//div[text()='This field is calculated upon save']");
        boolean elementVisible = getUtils().isElementVisible(authority);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyHeaderModalTextIsCorrect() {
       /* osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By headerText = By.xpath("//h2[text()=\"New SPA or Waiver: Medicaid SPA\"]");
        boolean elementVisible = getUtils().isElementVisible(headerText);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();*/

    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyIDNumberFieldIsPresent() {
       /* osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By IdNumber = By.xpath("//label[text()=\"ID Number\"]/following-sibling::div/input");
        boolean elementVisible = getUtils().isElementVisible(IdNumber);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/

    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyInitialSubmissionDateFieldIsPresent() {
      /*  osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By initialSubmissionDate = By.xpath("//label[text()=\"Initial Submission Date\"]/following-sibling::div/input");
        boolean elementVisible = getUtils().isElementVisible(initialSubmissionDate);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyStateDropdownIsPresent() {
     /*   osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By initialSubmissionDate = By.xpath("//label[text()=\"State\"]/parent::span/following-sibling::div[1]/lightning-base-combobox");
        boolean elementVisible = getUtils().isElementVisible(initialSubmissionDate);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/

    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyPresenceOfCancelButton() {
      /*  osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By cancelButton = By.xpath("//button[text()=\"Cancel\"]");
        boolean elementVisible = getUtils().isElementVisible(cancelButton);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyPresenceOfSaveButton() {
    /*    osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By save = By.xpath("//button[text()=\"Save\"]");
        boolean elementVisible = getUtils().isElementVisible(save);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }


    @Test(groups = {"Validations & Business Rules"})
    public void VerifyPresenceOfSaveAndNewButton() {
   /*     osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By saveAndNew = By.xpath("//button[text()=\"Save & New\"]");
        boolean elementVisible = getUtils().isElementVisible(saveAndNew);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void VerifyThatTheAsteriskAppearsNextToInitialSubmissionDateField() {
  /*      osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By date = By.xpath("//label[text()=\"Initial Submission Date\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(date);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void VerifyThatTheAsteriskAppearsNextToStateDropdown() {
/*        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By state = By.xpath("//label[text()=\"State\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(state);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }


    @Test(groups = {"Validations & Business Rules"})
    public void VerifyThatTheRequiredInformationTextIsDisplayed() {
       /* osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By infoText = By.xpath("//div[text()=\" = Required Information\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(infoText);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void VerifyThatIdentifyingInfoHeaderIsPresent() {
    /*    osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        By identifyingInfo = By.xpath("//span[text()=\"Identifying Information\"]/parent::h3");
        boolean elementVisible = getUtils().isElementVisible(identifyingInfo);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatAuthorityFieldIsReadOnlyInTheDetailsPage() {
      /*  osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
        boolean isEditAuthorityButtonInvisible = getUtils().isElementInvisible(By.xpath("//lightning-formatted-text[text()=\"Medicaid SPA\"]"));
        TestAssert.assertTrue(isEditAuthorityButtonInvisible, "Edit Authority button should not be visible");
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatPriorityCodeFieldIsRequiredToSaveSPADetails() throws InterruptedException {
/*        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
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
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();*/

    }

 /*   @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }*/
}
