package gov.cms.smart.tests.flows;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class WorkflowStatusTransitionTests extends BaseTest {

    @BeforeClass()
    public void setup() throws InterruptedException {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
        osgUser.goToSPAWaiversPage().searchSPA(spaPackage);
    }

    @BeforeMethod
    public void openRecord(){
        utils.openRecord(spaPackage.getPackageId());
    }

    @Test(groups = {"Workflow & Status Transitions"})
    public void verifyTypeAndSubtypeAreRemovedFromIdentifyingInformationSection() {
        boolean isTypeVisible = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).isTypeVisibleUnderIdentifyingInfo();
        boolean isSubtypeVisible = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).isSubTypeVisibleUnderIdentifyingInfo();
        TestAssert.assertFalse(
                isTypeVisible,
                "Type field should not be visible in Identifying Information section"
        );
        TestAssert.assertFalse(
                isSubtypeVisible,
                "Subtype field should not be visible in Identifying Information section"
        );
    }

    @Test(groups = {"Workflow & Status Transitions"})
    public void verifyTypeAndSubtypeAreDisplayedUnderSubmissionInformationSection() {
        boolean areFieldsDisplayedCorrectly = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).areFieldsGroupedCorrectly();
        TestAssert.assertTrue(areFieldsDisplayedCorrectly, "Type and Subtype fields are not displayed under Submission Information section");
    }

    @Test(groups = {"Workflow & Status Transitions"})
    public void verifySubStatusFieldIsPresentInCompletionStatusSection() {
        boolean isSubStatusPresent = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).isSubStatusDisplayedUnderCompletionStatus();
        TestAssert.assertTrue(
                isSubStatusPresent,
                "Sub-Status field is not present under Completion Status section"
        );
    }

    @AfterMethod()
    public void navigateToRecentlyViewed() throws InterruptedException {
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }

}
