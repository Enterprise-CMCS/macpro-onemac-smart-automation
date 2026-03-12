package gov.cms.smart.tests.functional.osg;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.testng.annotations.Test;


public class OSGTests extends BaseTest {

    /*   @BeforeClass()
       public void setup() {
           spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
           createDriverSession();
           osgUser = createNewOSGUser();
           osgUser.loginWithSharedSecret();
       }*/
    @Test
    public void loginTest() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }
 /*   @Test(groups = {})
    public void testSkip() {
        throw new SkipException("BLOCKED: Feature disabled / data not available / env issue");
    }*/

   /* @BeforeGroups(groups = {"EditRecord"})
    public void searchForRecord() throws InterruptedException {
        osgUser.goToSPAWaiversPage().searchSPA(spaPackage);
    }

    @BeforeMethod(onlyForGroups = "EditRecord")
    public void openRecordInEditMode() {
        utils.openRecord(spaPackage.getPackageId());
        utils.editByLabel("Priority Code");
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
    public void afterDetailsPageTests() throws InterruptedException {
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
    }


    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }*/
}
