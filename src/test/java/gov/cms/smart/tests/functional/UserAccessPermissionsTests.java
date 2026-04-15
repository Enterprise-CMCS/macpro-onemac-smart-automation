package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import static gov.cms.smart.pages.SPAsWaiversPage.NEW_BUTTON;

public class UserAccessPermissionsTests extends BaseTest {


    @BeforeMethod(onlyForGroups = "CPOC Tests")
    public void cpocTestsSetup() throws InterruptedException {
        createDriverSession();
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret();
    }

    @Test(groups = {"User Access & Permissions", "CPOC Tests"})
    public void verifyThatNewButtonIsDisplayedForCPOC() {
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
        TestAssert.assertTrue(utils.isVisible(NEW_BUTTON), "");
    }

    @AfterMethod(onlyForGroups = "CPOC Tests")
    public void cpocTestsCleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }

    @BeforeGroups({"SRT Tests"})
    public void srtTestsSetup() throws InterruptedException {
        spaPackage = ExcelPackageSelector.selectSpa("CO", "Medicaid SPA", "");
        createDriverSession();
        srtUser = createNewSRTUser();
        srtUser.loginWithSharedSecret();
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().searchSPA(spaPackage);
        utils.openRecord(spaPackage.getPackageId());
    }

    @BeforeMethod(onlyForGroups = "SPA or Waivers")
    public void goToSpaAndWaivers(){
        PageFactory.getHomePage(getDriver(),getUtils()).goToSpasWaiversPage();
    }

    @Test(groups = {"User Access & Permissions", "SRT Tests","SPA or Waivers"}, priority = 5)
    public void checkButtonIsNotDisplayedSRT(){
        TestAssert.assertFalse(srtUser.isNewButtonPresent(), "");
    }

    @Test(groups = {"User Access & Permissions", "SRT Tests"}, priority = 1)
    public void checkPriorityFieldSRT() {
        By priorityCode = By.xpath("//span[text()=\"Priority Code\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(priorityCode), "SRT should not be able to edit field");
    }

    @Test(groups = {"User Access & Permissions", "SRT Tests"}, priority = 2)
    public void checkApprovedEffectiveDateSRTUser() {
        By approvedEffectiveDate = By.xpath("//span[text()=\"Approved Effective Date\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(approvedEffectiveDate), "SRT should not be able to edit field");
    }

    @Test(groups = {"User Access & Permissions", "SRT Tests"}, priority = 3)
    public void checkCodingAfterInitialAssessmentSRTUser() {
        By codingAfterInitialAssessment = By.xpath("//span[text()=\"Coding After Initial Assessment\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(codingAfterInitialAssessment), "SRT should not be able to edit field");
    }

    @Test(groups = {"User Access & Permissions", "SRT Tests"}, priority = 4)
    public void checkInitialSubmissionCompleteSRTUser() {
        By initialSubmissionComplete = By.xpath("//span[text()=\"Initial Submission Complete\"]/../following-sibling::div//button");
        TestAssert.assertFalse(utils.isVisible(initialSubmissionComplete), "SRT should not be able to edit field");
    }

    @AfterGroups("SRT Tests")
    public void srtTestsCleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }

    @BeforeMethod(onlyForGroups = "OSG Tests")
    public void osgTestsSetup() throws InterruptedException {
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }

    @Test(groups = {"User Access & Permissions", "OSG Tests"})
    public void verifyThatNewButtonIsDisplayedOSG() throws InterruptedException {
        osgUser.goToSPAWaiversPage();
        TestAssert.assertTrue(osgUser.isNewButtonPresent(), "");
    }

    @AfterMethod(onlyForGroups = "OSG Tests")
    public void osgTestsCleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }


}
