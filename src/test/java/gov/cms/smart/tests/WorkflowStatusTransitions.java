package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WorkflowStatusTransitions extends BaseTest {

    /*@BeforeClass
    public void setup() {
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
        spaPackage = ExcelPackageSelector.selectSpa("CO", "Medicaid SPA", "");
    }*/

    @Test(groups = {"Workflow & Status Transitions"})
    public void verifyTypeAndSubtypeAreRemovedFromIdentifyingInformationSection() {
       /* boolean isTypeVisible = PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().
                openExistingRecord(spaPackage).isTypeVisibleUnderIdentifyingInfo();
        boolean isSubtypeVisible = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).isSubTypeVisibleUnderIdentifyingInfo();

        TestAssert.assertFalse(
                isTypeVisible,
                "Type field should not be visible in Identifying Information section"
        );
        TestAssert.assertFalse(
                isSubtypeVisible,
                "Subtype field should not be visible in Identifying Information section"
        );*/
    }

    @Test(groups = {"Workflow & Status Transitions"})
    public void verifyTypeAndSubtypeAreDisplayedUnderSubmissionInformationSection() {
    /*    boolean areFieldsDisplayedCorrectly = PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().
                openExistingRecord(spaPackage).isTypeAndSubTypeVisibleUnderSubmissionInfo();
        TestAssert.assertTrue(areFieldsDisplayedCorrectly, "Type and Subtype fields are not displayed under Submission Information section");*/
    }

    @Test(groups = {"Workflow & Status Transitions"})
    public void verifySubStatusFieldIsPresentInCompletionStatusSection() {
     /*   boolean isSubStatusPresent = PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().
                openExistingRecord(spaPackage).isSubStatusDisplayedUnderCompletionStatus();
        TestAssert.assertTrue(
                isSubStatusPresent,
                "Sub-Status field is not present under Completion Status section"
        );*/
    }


   /* @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }*/

}
