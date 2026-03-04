package gov.cms.smart.tests.functional.osg;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

import static gov.cms.smart.pages.DetailsTab.*;

public class ValidationAndBusinessRuleTests extends BaseTest {
  /*  @BeforeClass()
    public void loginAsOSG() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }


    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatSubtypeIsDisabledWhenTypeIsAdmin() throws InterruptedException {
        osgUser.goToSPAWaiversPage().openRecordFromAllRecords(spaPackage);
        utils.editByLabel("Priority Code");
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

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatPriorityCodeFieldIsRequiredToSaveSPADetails() throws InterruptedException {
        osgUser.goToSPAWaiversPage().openRecordFromAllRecords(spaPackage);
        utils.editByLabel("Priority Code");
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
        PageFactory.getSpaDetailsPage(getDriver(),utils).clickCancel();
    }


    @Test(groups = {"Validations & Business Rules"})
    public void verifySubtypeLabelFormattingIsCorrect() throws InterruptedException {
        osgUser.goToSPAWaiversPage().openRecordFromAllRecords(spaPackage);
        getUtils().scrollToElement(MILESTONES_SECTION);
        TestAssert.assertEquals(getDriver(), SUB_TYPE_SI, "Subtype", "Subtype label text or formatting is incorrect");
    }

    @Test(groups = {"Validations & Business Rules"})
    public void verifyThatAuthorityFieldIsReadOnlyInTheDetailsPage() throws InterruptedException {
        osgUser.goToSPAWaiversPage().openRecordFromAllRecords(spaPackage);
        getUtils().scrollToElement(IDENTIFYING_INFORMATION);
        boolean isFieldEditable = getUtils().isVisible(By.xpath("//flexipage-field[@data-field-id='RecordSMART_CMCS_Authority__cField']//span[contains(@class,'is-read-only')]"));
        TestAssert.assertTrue(isFieldEditable, "Field Should not be editable");
    }

    @AfterClass()
    public void closeBrowser() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }*/

    /*@Test(groups = {})
    public void testSkip() {
        throw new SkipException("BLOCKED: Feature disabled / data not available / env issue");
    }*/
}



