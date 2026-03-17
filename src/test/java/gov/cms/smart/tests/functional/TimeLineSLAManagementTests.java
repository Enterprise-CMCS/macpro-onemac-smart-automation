package gov.cms.smart.tests.functional;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.pages.DetailsTab;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import static gov.cms.smart.pages.DetailsTab.*;

public class TimeLineSLAManagementTests extends BaseTest {

    @BeforeClass()
    public void setup() {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
        PageFactory.getHomePage(getDriver(), getUtils()).
                goToSpasWaiversPage().searchSPA(spaPackage);
    }

    @BeforeMethod
    public void openRecord() {
        utils.openRecord(spaPackage.getPackageId());
        utils.editByLabel("Priority Code");
    }

    //<---->Timeline & SLA Management Scripts Start here<---->
    @Test(groups = {"Timeline & SLA Management"})
    public void verifyWarningBannerAppearsWhenSubmissionVerifiedCompleteIsBlankAndMoreThan5DaysPassed() throws InterruptedException {
        utils.sendKeysToInputByLabel("Initial Submission Date", utils.getPastDate(6));
        utils.selectFromComboBoxByLabel("Initial Submission Complete", "No");
        utils.clearInputByLabel("Submission Verified Complete");
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        TestAssert.assertTrue(utils.isElementVisible(WARNING_BANNER), "Banner should be displayed");
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verifyWarningBannerDoesNotAppearWhenLessThanFiveDaysPassed() {
        DetailsTab spaDetailsPage = PageFactory.getSpaDetailsPage(getDriver(), getUtils());
        // Ensure Submission Verified Complete is blank
        utils.clearInputByLabel("Submission Verified Complete");
        // Set Initial Submission Date to less than 5 days ago (example: 3 days)
        utils.sendKeysToInputByLabel("Initial Submission Date", utils.getPastDate(3));
        spaDetailsPage.save();
        getDriver().navigate().refresh();
        TestAssert.assertFalse(utils.isElementVisible(WARNING_BANNER),
                "Warning banner should NOT appear when less than 5 days have passed since Initial Submission Date."
        );
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verifyWarningBannerDoesNotAppearWhenSubmissionVerifiedCompleteHasDate() {
        String initialSubmissionDate = utils.getPastDate(6);
        String submissionVerifiedCompleteDate = utils.getPastDate(2);
        DetailsTab spaDetailsPage = PageFactory.getSpaDetailsPage(getDriver(), getUtils());
        // Set Initial Submission Date to more than 5 days ago
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        // Enter Submission Verified Complete date
        utils.clearInputByLabel("Submission Verified Complete");
        utils.sendKeysToInputByLabel("Submission Verified Complete", submissionVerifiedCompleteDate);
        spaDetailsPage.save();
        getDriver().navigate().refresh();
        TestAssert.assertFalse(
                utils.isElementVisible(WARNING_BANNER),
                "Warning banner should NOT appear when Submission Verified Complete has a date."
        );

    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verifyDaysOnActiveClockLabelDisplayedAndOldLabelNotDisplayed() {
        // Verify new label appears
        TestAssert.assertTrue(
                utils.isElementVisible(DAYS_ON_ACTIVE_CLOCK_HEADER),
                "Label 'Days on Active Clock' should be displayed in the header."
        );
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verifyDateOnActiveClockUpdatesAutomatically() {
        String initialSubmissionDate = utils.getPastDate(6);
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        String expectedDate = utils.calculateDaysFromToday(initialSubmissionDate);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        TestAssert.assertEquals(getDriver(), DAYS_ON_ACTIVE_CLOCK_VALUE, expectedDate, "");
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verifyDaysRemainingUpdatesAutomatically() {
        String initialSubmissionDate = utils.getPastDate(5);
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        String daysOnActiveClock = utils.getText(DAYS_ON_ACTIVE_CLOCK_VALUE);
        String expectedDaysRemaining = utils.calculateDaysRemaining(daysOnActiveClock);
        TestAssert.assertEquals(getDriver(), REMAINING_DAYS, expectedDaysRemaining, "");
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verify90thDayCalculatedCorrectly() {
        String initialSubmissionDate = utils.getPastDate(5);
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        String actual = utils.getFieldTextByLabel("90th Day");
        String expected = utils.calculateDate(initialSubmissionDate, 90);
        TestAssert.assertEquals(actual, expected, "");
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verify15DayMilestoneCalculatedCorrectly() {
        String initialSubmissionDate = utils.getPastDate(5);
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        utils.scrollToElement(SUBMISSION_INFORMATION_SECTION);
        String actual = utils.getFieldTextByLabel("15th Day");
        String expected = utils.calculateDate(initialSubmissionDate, 15);
        TestAssert.assertEquals(actual, expected, "");
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verify45DayMilestoneCalculatedCorrectly() {
        String initialSubmissionDate = utils.getPastDate(5);
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        utils.scrollToElement(SUBMISSION_INFORMATION_SECTION);
        String actual = utils.getFieldTextByLabel("45th Date");
        String expected = utils.calculateDate(initialSubmissionDate, 45);
        TestAssert.assertEquals(actual, expected, "");
    }

    @Test(groups = {"Timeline & SLA Management"})
    public void verify76DayMilestoneCalculatedCorrectly() {
        String initialSubmissionDate = utils.getPastDate(5);
        utils.clearInputByLabel("Initial Submission Date");
        utils.sendKeysToInputByLabel("Initial Submission Date", initialSubmissionDate);
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).save();
        getDriver().navigate().refresh();
        utils.scrollToElement(SUBMISSION_INFORMATION_SECTION);
        String actual = utils.getFieldTextByLabel("76th Day");
        String expected = utils.calculateDate(initialSubmissionDate, 76);
        TestAssert.assertEquals(actual, expected, "");
    }

    @AfterMethod()
    public void navigateToRecentlyViewed() {
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
