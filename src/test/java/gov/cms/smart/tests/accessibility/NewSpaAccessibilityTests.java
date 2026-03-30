package gov.cms.smart.tests.accessibility;


import gov.cms.smart.base.BaseAccessibilityTest;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewSpaAccessibilityTests extends BaseAccessibilityTest {

    @BeforeClass()
    public void setup() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret().goToSpasWaiversPage().searchSPA(spaPackage);
        utils.openRecord(spaPackage.getPackageId());
        utils.editByLabel("Priority Code");
    }

    @Test(groups = {"Accessibility (Section 508 Compliance)"})
    public void verifyEditRecordAccessibility() {
        runAccessibilityCheck();
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}