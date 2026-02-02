package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.AssertionUtil;
import gov.cms.smart.utils.ExcelPackageSelector;
import gov.cms.smart.utils.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MedicaidSPATests extends BaseTest {

 /*   @DataProvider
    public Object[][] priorityLevels() {
        return new Object[][]{
                {PriorityCode.ESCALATED_REVIEW, CodingAssessment.SAME},
                {PriorityCode.CUSTOMARY_REVIEW, CodingAssessment.UP_CODED},
                {PriorityCode.EXPEDITED_REVIEW, CodingAssessment.DOWN_CODED}
        };
    }

    @Test(dataProvider = "priorityLevels")
    public void verifyMedicaidSPAPriorityInfoWithDifferentPrioritiesAndCodingAssessment(PriorityCode priorityCode, CodingAssessment codingAssessment) {
        OSGUser osgUser = createNewOSGUser();

        osgUser.
                navigateToSalesForce().
                loginAsOSGUser().
                goToSpasWaiversPage().openExistingRecord("AL", "Medicaid SPA");

        PriorityInfo actual = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).fillPriorityInfo(priorityCode, codingAssessment);
        PriorityInfo expected = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).readPriorityInfo();
        AssertionUtil.assertEquals(actual, expected, "");

    }*/


    @Test
    public void verifyPlanInformation() {
        OSGUser osgUser = createNewOSGUser();
        osgUser.
                navigateToSalesForce();
              /*  loginAsOSGUser().
                goToSpasWaiversPage().openExistingRecord("AL", "Medicaid SPA");
        PlanInfo actual = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).fillPlanInfo("Test Subject", "Test Description");
        PlanInfo expected = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).readPlanInfo();
        AssertionUtil.assertEquals(actual,expected,"");*/
    }

  /*  @Test
    public void verifyindentifyinginfomedicaidspa() {
        OSGUser osgUser = createNewOSGUser();
        SpaPackage spaPackage = osgUser.
                navigateToSalesForce().
                loginAsOSGUser().
                goToSpasWaiversPage().openExistingRecord("AL", "Medicaid SPA");
        IdentifyingInfo actual = new IdentifyingInfo();
        actual.setAuthority(spaPackage.getAuthority());
        actual.setIdNumber(spaPackage.getPackageId());
        String state = getUtils().getStateFullName(spaPackage.getState());
        actual.setState(state);
        IdentifyingInfo expected = PageFactory.getSpaDetailsPage(getDriver(), getUtils()).readIdentifyingInfo();
        AssertionUtil.assertEquals(actual,expected,"");
    }*/




  /*  @Test
    public void testEmptyFormSubmission() {
        OSGUser osgUser = createNewOSGUser();
        osgUser.navigateToSalesForce();
        osgUser.login();
        osgUser.goToSPAWaiversPage();
        osgUser.navigateToMedicaidSPAForm();
        AssertionUtil.assertTrue(osgUser.isIDNumberErrorDisplayed(), "ID Number error should show.");
        AssertionUtil.assertTrue(osgUser.isInitialSubmissionDateErrorDisplayed(), "Date error should show.");
        AssertionUtil.assertTrue(osgUser.isStateErrorDisplayed(), "State error should show");
    }*/

   /* @Test
    public void testInvalidDateFormat() throws InterruptedException {
        OSGUser osgUser = createNewOSGUser();
        osgUser.navigateToSalesForce();
        osgUser.login();
        osgUser.goToSPAWaiversPage();
        osgUser.navigateToMedicaidSPAForm();
        osgUser.enterInitialSubmissionDate("1234");
        boolean isDateFormateErrorDisplayed = osgUser.isDateFormatErrorDisplayed();
        AssertionUtil.assertTrue(isDateFormateErrorDisplayed, "Date format error should be displayed.");
    }*/
/*
    @Test
    public void testValidSubmission() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("ID Number"), "ID Number should not have error");
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("Initial Submission Date"), "Date should not have error");
        Assert.assertFalse(waiverForm.isErrorDisplayedForField("State"), "State should not have error");
        Assert.assertTrue(waiverForm.isFormSubmittedSuccessfully(), "Form should submit successfully");
    }

    @Test
    public void testEmptyIdNumber() {
        waiverForm.enterIdNumber("");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("ID Number"), "ID Number error expected");
    }

    @Test
    public void testEmptyDate() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("");
        waiverForm.selectState("CA");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("Initial Submission Date"), "Date error expected");
    }

    @Test
    public void testEmptyState() {
        waiverForm.enterIdNumber("12345");
        waiverForm.enterInitialSubmissionDate("12/31/2024");
        waiverForm.selectState("--None--");
        waiverForm.clickSave();
        Assert.assertTrue(waiverForm.isErrorDisplayedForField("State"), "State error expected");
    }*/
}
