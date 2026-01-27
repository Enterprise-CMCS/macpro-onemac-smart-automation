package gov.cms.smart.tests;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.flows.OSGUser;
import gov.cms.smart.utils.AssertionUtil;
import org.testng.annotations.Test;

public class MedicaidSPATests extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        OSGUser osgUser = createNewOSGUser();
        osgUser.navigateToSalesForce();
        osgUser.login();
        osgUser.modelTest();
    }


    @Test
    public void testEmptyFormSubmission() {
        OSGUser osgUser = createNewOSGUser();
        osgUser.navigateToSalesForce();
        osgUser.login();
       /*osgUser.goToSPAWaiversPage();
        osgUser.navigateToMedicaidSPAForm();
        AssertionUtil.assertTrue(osgUser.isIDNumberErrorDisplayed(), "ID Number error should show.");
        AssertionUtil.assertTrue(osgUser.isInitialSubmissionDateErrorDisplayed(), "Date error should show.");
        AssertionUtil.assertTrue(osgUser.isStateErrorDisplayed(), "State error should show");*/
    }

    @Test
    public void testInvalidDateFormat() throws InterruptedException {
        OSGUser osgUser = createNewOSGUser();
        osgUser.navigateToSalesForce();
        osgUser.login();
        osgUser.goToSPAWaiversPage();
        osgUser.navigateToMedicaidSPAForm();
        osgUser.enterInitialSubmissionDate("1234");
        boolean isDateFormateErrorDisplayed = osgUser.isDateFormatErrorDisplayed();
        AssertionUtil.assertTrue(isDateFormateErrorDisplayed, "Date format error should be displayed.");
    }
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
