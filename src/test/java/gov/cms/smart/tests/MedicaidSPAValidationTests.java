package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MedicaidSPAValidationTests extends BaseTest {

    SpaPackage spaPackage;
    OSGUser osgUser;

    @BeforeClass
    public void setup() {
        createDriverSession();
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
    }

    @Test
    public void preparePriorityData() {
        osgUser.goToSPAWaiversPage().openExistingRecord(spaPackage);
    }

   /* @DataProvider
    public Object[][] priorityLevels() {
        return new Object[][]{
                {PriorityCode.ESCALATED_REVIEW, CodingAssessment.SAME},
                {PriorityCode.CUSTOMARY_REVIEW, CodingAssessment.UP_CODED},
                {PriorityCode.EXPEDITED_REVIEW, CodingAssessment.DOWN_CODED}
        };
    }

    @Test(dataProvider = "priorityLevels",
            dependsOnMethods = "preparePriorityData")
    public void verifyMedicaidSPAPriorityInfoWithDifferentPrioritiesAndCodingAssessment(PriorityCode priorityCode, CodingAssessment codingAssessment) throws InterruptedException {
        PriorityInfo actual = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).
                fillPriorityInfo(priorityCode, codingAssessment);
        PriorityInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPriorityInfo();
        TestAssert.assertEquals(actual, expected, "");
    }*/

/*    @Test(dependsOnMethods = "preparePriorityData")
    public void verifyPlanInformation() {
        PlanInfo actual = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils())
                .fillPlanInfo("Test Subject", "Test Description");
        PlanInfo expected = PageFactory.
                getSpaDetailsPage(getDriver(), getUtils()).
                readPlanInfo();
        TestAssert.assertEquals(actual, expected, "Should save plan information");
    }

    @Test(dependsOnMethods = "preparePriorityData")
    public void verifyindentifyinginfomedicaidspa() {
        IdentifyingInfo actual = new IdentifyingInfo();
        actual.setAuthority(spaPackage.getAuthority());
        actual.setIdNumber(spaPackage.getPackageId());
        String state = getUtils().getStateFullName(spaPackage.getState());
        actual.setState(state);
        IdentifyingInfo expected = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).readIdentifyingInfo();
        TestAssert.assertEquals(actual, expected, "Should save identifying information.");
    }

    @Test
    public void testEmptyFormSubmission() {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        TestAssert.assertTrue(osgUser.isInitialSubmissionDateErrorDisplayed(), "Date error should show.");
        TestAssert.assertTrue(osgUser.isStateErrorDisplayed(), "State error should show");
        TestAssert.assertTrue(osgUser.isIDNumberErrorDisplayed(), "ID Number error should show.");
    }

    @Test
    public void testInvalidDateFormat() throws InterruptedException {
        osgUser.goToSPAWaiversPage().clickNew().navigateToMedicaidSPAForm();
        osgUser.enterInitialSubmissionDate("1234");
        boolean isDateFormateErrorDisplayed = osgUser.isDateFormatErrorDisplayed();
        TestAssert.assertTrue(isDateFormateErrorDisplayed, "Date format error should be displayed.");
    }*/

    /*@Test
    public void testValidSubmission() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("ID Number"), "ID Number should not have error");
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("Initial Submission Date"), "Date should not have error");
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("State"), "State should not have error");
        Assert.assertTrue(waiverForm.isFormSubmittedSuccessfully(), "Form should submit successfully");
    }*/

    /*@Test
    public void testEmptyIdNumber() {
        waiverForm.enterIdNumber("");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("ID Number"), "ID Number error expected");
    }*/

  /*  @Test
    public void testEmptyDate() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("Initial Submission Date"), "Date error expected");
    }*/

 /*   @Test
    public void testEmptyState() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("--None--");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("State"), "State error expected");
    }*/

    @AfterClass(alwaysRun = true)
    public void cleanUp() throws InterruptedException {
        WebDriver d = getDriver();
        if (d != null) {
            Thread.sleep(5000);
            d.quit();
        }
    }
}
