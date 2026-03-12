package gov.cms.smart.tests.functional.osg;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class NewSPAFormTests extends BaseTest {
    @BeforeClass()
    public void setup() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
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
