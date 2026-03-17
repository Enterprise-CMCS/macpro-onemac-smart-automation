package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.dataproviders.DataProviders;
import gov.cms.smart.pages.DetailsTab;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static gov.cms.smart.dataproviders.DataProviders.STATUS_TO_SUBSTATUS;
import static gov.cms.smart.dataproviders.DataProviders.TYPE_TO_SUBTYPE;
import static gov.cms.smart.pages.DetailsTab.*;
import static gov.cms.smart.pages.DetailsTab.IDENTIFYING_INFORMATION;

public class ValidationAndBusinessRulesTests extends BaseTest {
    /*   @Test(groups = {})
       public void testSkip() {
           throw new SkipException("BLOCKED: Feature disabled / data not available / env issue");
       }*/
    @BeforeClass()
    public void setup() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }


    @BeforeGroups(groups = {"EditRecord"})
    public void searchForRecord() throws InterruptedException {
        osgUser.goToSPAWaiversPage().searchSPA(spaPackage);
    }

    @BeforeMethod(onlyForGroups = "EditRecord")
    public void openRecordInEditMode() {
        try {
            Assert.assertNotNull(spaPackage, "spaPackage is null in @BeforeMethod");
            Assert.assertNotNull(spaPackage.getPackageId(), "PackageId is null in @BeforeMethod");

            utils.waitForSalesforceLoading(getDriver());
            utils.openRecord(spaPackage.getPackageId());
            utils.waitForSalesforceLoading(getDriver());

            DetailsTab spaDetailsPage = PageFactory.getSpaDetailsPage(getDriver(), getUtils());
            utils.editByLabel("Priority Code");
            utils.waitForSalesforceLoading(getDriver());

        } catch (Exception e) {
            String screenshotPath = null;
            try {
                screenshotPath = utils.takeScreenshot("beforemethod_openRecordInEditMode_failed");
            } catch (Exception ignored) {
            }

            throw new AssertionError(
                    "Precondition failed in @BeforeMethod openRecordInEditMode. "
                            + "packageId=" + (spaPackage != null ? spaPackage.getPackageId() : "null")
                            + (screenshotPath != null ? ", screenshot=" + screenshotPath : "")
                            + ", error=" + e.getMessage(),
                    e
            );
        }
    }

    @Test(groups = {"Validations & Business Rules", "EditRecord"}, dataProvider = "statusSubstatusData", dataProviderClass = DataProviders.class)
    public void verifySubStatusListFiltersCorrectlyForEachStatus(String status) throws InterruptedException {
        setTestName("status", status);
        utils.selectFromComboBoxByLabel("Status", status);
        List<String> actualOptions =
                utils.getValuesFromDropdownByLabel("Sub-Status");
        List<String> expectedOptions =
                STATUS_TO_SUBSTATUS.get(status);
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
                "Sub-Status mismatch for Status: " + status +
                        "\nMissing: " + missing +
                        "\nExtra: " + extra
        );
    }

    @Test(groups = {"Validations & Business Rules", "EditRecord"}, dataProvider = "typeSubtypeData", dataProviderClass = DataProviders.class)
    public void verifySubTypeListFiltersCorrectlyForEachType(String type) throws InterruptedException {
        setTestName("type", type);
        utils.selectFromComboBoxByLabel("Type", type);
        List<String> actualOptions =
                utils.getValuesFromDropdownByLabel("Subtype");

        List<String> expectedOptions =
                TYPE_TO_SUBTYPE.get(type);
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
                "SubType mismatch for Type: " + type +
                        "\nMissing: " + missing +
                        "\nExtra: " + extra
        );
    }

    @Test(groups = {"Validations & Business Rules", "EditRecord"})
    public void verifyThatSubtypeIsDisabledWhenTypeIsAdmin() throws InterruptedException {
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

    }

    @Test(groups = {"Validations & Business Rules", "EditRecord"})
    public void verifyThatPriorityCodeFieldIsRequiredToSaveSPADetails() throws InterruptedException {
        getUtils().selectFromComboBoxByLabel("Priority Code", "--None--");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        By priorityCodeCombobox = By.xpath("//label[text()='Priority Code']/ancestor::lightning-combobox");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        // Wait until 'invalid' attribute appears
        wait.until(driver -> driver.findElement(priorityCodeCombobox).getAttribute("invalid") != null);
        // Assert that the attribute is present
        WebElement element = getDriver().findElement(priorityCodeCombobox);
        boolean isInvalid = element.getAttribute("invalid") != null;
        TestAssert.assertTrue(isInvalid, "Priority Code field should be invalid");
    }

    @AfterMethod(onlyForGroups = "EditRecord")
    public void resetToHomePageAfterDropdownTests() throws InterruptedException {
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        utils.waitForInvisibility(FIELD);
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
    }

    @BeforeMethod(onlyForGroups = "DetailsPageTests")
    public void openRecord() throws InterruptedException {
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().openExistingRecord(spaPackage);
    }

    @Test(groups = {"Validations & Business Rules", "DetailsPageTests"})
    public void verifySubtypeLabelFormattingIsCorrect() {
        getUtils().scrollToElement(MILESTONES_SECTION);
        TestAssert.assertEquals(getDriver(), SUB_TYPE_SI, "Subtype", "Subtype label text or formatting is incorrect");
    }

    @Test(groups = {"Validations & Business Rules", "DetailsPageTests"})
    public void verifyThatAuthorityFieldIsReadOnlyInTheDetailsPage() {
        getUtils().scrollToElement(IDENTIFYING_INFORMATION);
        boolean isFieldEditable = getUtils().isVisible(By.xpath("//flexipage-field[@data-field-id='RecordSMART_CMCS_Authority__cField']//span[contains(@class,'is-read-only')]"));
        TestAssert.assertTrue(isFieldEditable, "Field Should not be editable");
    }

    @AfterMethod(onlyForGroups = "DetailsPageTests")
    public void afterDetailsPageTests(){
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
    }
    //Start of New SPA Form Validations Tests
    @BeforeMethod(onlyForGroups = "New SPA Form Validations")
    public void NavigateToNewSPAForm() throws InterruptedException {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void testEmptyFormSubmission() {
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
    }


    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void testInvalidDateFormat() throws InterruptedException {
        osgUser.enterInitialSubmissionDate("1234");
        boolean isDateFormateErrorDisplayed = osgUser.isDateFormatErrorDisplayed();
        TestAssert.assertTrue(isDateFormateErrorDisplayed, "Date format error should be displayed.");
    }


    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void testAuthorityFieldIsReadOnlyInNewSPAWaiversForm() {
        By authority = By.xpath("//div[text()='This field is calculated upon save']");
        boolean elementVisible = getUtils().isElementVisible(authority);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void verifyHeaderModalTextIsCorrect() {
        By headerText = By.xpath("//h2[text()=\"New SPA or Waiver: Medicaid SPA\"]");
        boolean elementVisible = getUtils().isElementVisible(headerText);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void verifyIDNumberFieldIsPresent() {
        By IdNumber = By.xpath("//label[text()=\"ID Number\"]/following-sibling::div/input");
        boolean elementVisible = getUtils().isElementVisible(IdNumber);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");

    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void verifyInitialSubmissionDateFieldIsPresent() {
        By initialSubmissionDate = By.xpath("//label[text()=\"Initial Submission Date\"]/following-sibling::div/input");
        boolean elementVisible = getUtils().isElementVisible(initialSubmissionDate);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void verifyStateDropdownIsPresent() {
        By initialSubmissionDate = By.xpath("//label[text()=\"State\"]/parent::span/following-sibling::div[1]/lightning-base-combobox");
        boolean elementVisible = getUtils().isElementVisible(initialSubmissionDate);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");

    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void verifyPresenceOfCancelButton() {
        By cancelButton = By.xpath("//button[text()=\"Cancel\"]");
        boolean elementVisible = getUtils().isElementVisible(cancelButton);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void verifyPresenceOfSaveButton() {
        By save = By.xpath("//button[text()=\"Save\"]");
        boolean elementVisible = getUtils().isElementVisible(save);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }


    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void VerifyPresenceOfSaveAndNewButton() {
        By saveAndNew = By.xpath("//button[text()=\"Save & New\"]");
        boolean elementVisible = getUtils().isElementVisible(saveAndNew);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void VerifyThatTheAsteriskAppearsNextToInitialSubmissionDateField() {
        By date = By.xpath("//label[text()=\"Initial Submission Date\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(date);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void VerifyThatTheAsteriskAppearsNextToStateDropdown() {
        By state = By.xpath("//label[text()=\"State\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(state);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }


    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void VerifyThatTheRequiredInformationTextIsDisplayed() {
        By infoText = By.xpath("//div[text()=\" = Required Information\"]/abbr[text()=\"*\"]");
        boolean elementVisible = getUtils().isElementVisible(infoText);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @Test(groups = {"Validations & Business Rules", "New SPA Form Validations"})
    public void VerifyThatIdentifyingInfoHeaderIsPresent() {
        By identifyingInfo = By.xpath("//span[text()=\"Identifying Information\"]/parent::h3");
        boolean elementVisible = getUtils().isElementVisible(identifyingInfo);
        TestAssert.assertTrue(elementVisible, "Element is not displayed");
    }

    @AfterMethod(onlyForGroups = "New SPA Form Validations")
    public void closeForm() {
        PageFactory.getNewSPAPage(getDriver(), getUtils()).clickCancelButton();
    }
    //End of New SPA Form Validations Tests

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}
