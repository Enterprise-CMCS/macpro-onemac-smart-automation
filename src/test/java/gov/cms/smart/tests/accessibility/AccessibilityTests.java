package gov.cms.smart.tests.accessibility;


import gov.cms.smart.base.BaseAccessibilityTest;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class AccessibilityTests extends BaseAccessibilityTest {

    @BeforeClass()
    public void setup() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }

    @BeforeMethod(onlyForGroups = "RecordDetails")
    public void SearchSPA() {
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().searchSPA(spaPackage);
        utils.openRecord(spaPackage.getPackageId());
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)", "RecordDetails"})
    public void verifyEditRecordAccessibility() {
        utils.editByLabel("Priority Code");
        runAccessibilityCheck();
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)"})
    public void verifyHomeTabAccessibility() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();
        runAccessibilityCheck();
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)", "RecordDetails"})
    public void verifyReviewTabAccessibility() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToReviewTab();
        runAccessibilityCheck();
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)", "RecordDetails"})
    public void verifyFinancialInformationTabAccessibility() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToFinancialInformationTab();
        runAccessibilityCheck();
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)", "RecordDetails"})
    public void verifyNotesTabAccessibility() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToNotesTab();
        runAccessibilityCheck();
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)", "RecordDetails"})
    public void verifyAdjudicationTabAccessibility() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToAdjudicationTab();
        runAccessibilityCheck();
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)"})
    public void verifyNewMedicaidSPAFormAccessibility() {
        PageFactory.getHomePage(getDriver(),getUtils()).goToSpasWaiversPage().clickNew().navigateToMedicaidSPAForm();
        runAccessibilityCheck();
        PageFactory.getNewSPAPage(getDriver(),getUtils()).clickCancelButton();
    }

    @AfterMethod
    public void navigateBackToHomePage() {
        PageFactory.getSpaWaiversPage(getDriver(), getUtils()).goToHomePage();
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}