package gov.cms.smart.tests.functional.cpoc;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.dataproviders.ValidationDataProviders;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static gov.cms.smart.dataproviders.ValidationDataProviders.GROUP_TO_DIVISIONS;

public class CPOCSPADetailsTests extends BaseTest {
/*
    @BeforeClass
    public void setSPAPackage() throws InterruptedException {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        cpocUser = createNewCPOCUser();
        cpocUser.loginWithSharedSecret().
                goToSpasWaiversPage().
                openRecordFromAllRecords(spaPackage).
                goToReviewTab();
    }


    @Test(groups = {"Validations & Business Rules"}, dataProvider = "groupDivisionData", dataProviderClass = ValidationDataProviders.class)
    public void verifyDivisionListFiltersCorrectlyForEachGroup(String group) throws InterruptedException {
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
    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        WebDriver d = getDriver();
        if (d != null) {
            d.quit();
        }
    }*/
}
