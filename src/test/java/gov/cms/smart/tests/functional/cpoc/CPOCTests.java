package gov.cms.smart.tests.functional.cpoc;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.dataproviders.DataProviders;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

import static gov.cms.smart.dataproviders.DataProviders.GROUP_TO_DIVISIONS;

public class CPOCTests extends BaseTest {

    @BeforeClass
    public void setup() {
        createDriverSession();
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret();
    }

    @BeforeGroups(groups = "groupDivisionValidation")
    public void NavigateToReviewTab() throws InterruptedException {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().openExistingRecord(spaPackage).goToReviewTab();
    }

    @Test(groups = {"Validations & Business Rules", "groupDivisionValidation"}, dataProvider = "groupDivisionData", dataProviderClass = DataProviders.class)
    public void verifyDivisionListFiltersCorrectlyForEachGroup(String group) throws InterruptedException {
        setTestName("group", group);
        utils.selectFromComboBoxByLabel("Group", group);
        List<String> actualOptions =
                utils.getValuesFromDropdownByLabel("Division");
        List<String> expectedOptions =
                GROUP_TO_DIVISIONS.get(group);
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
                "Division mismatch for group: " + group +
                        "\nMissing: " + missing +
                        "\nExtra: " + extra
        );
    }

    @AfterGroups(groups = "groupDivisionValidation")
    public void navigateBack() {
        PageFactory.getHomePage(getDriver(), getUtils());
    }

   /* @Test(groups = {"User Access & Permissions"})
    public void verifyCPOCUserCanCreateMedicaidSPARecord() throws InterruptedException {
        boolean isSPACreated = PageFactory.
                getHomePage(getDriver(), getUtils()).goToSpasWaiversPage().clickNew().
                createSPA("CO", "Medicaid SPA");
        TestAssert.assertTrue(isSPACreated, "SPA creation failed - success confirmation message was not displayed");
    }*/

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }
}
