package gov.cms.smart.tests.functional.osg;

import gov.cms.smart.base.BaseTest;
import gov.cms.smart.dataproviders.ValidationDataProviders;
import gov.cms.smart.utils.assertions.TestAssert;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static gov.cms.smart.dataproviders.ValidationDataProviders.STATUS_TO_SUBSTATUS;
import static gov.cms.smart.dataproviders.ValidationDataProviders.TYPE_TO_SUBTYPE;

public class DataDrivenTests extends BaseTest {
/*
    @BeforeClass()
    public void loginAsOSG() throws InterruptedException {
        spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        createDriverSession();
        osgUser = createNewOSGUser();
        osgUser.loginWithSharedSecret();
        osgUser.goToSPAWaiversPage().openRecordFromAllRecords(spaPackage);
    }

    @BeforeMethod(onlyForGroups = "Dropdown Validations")
    public void enterEditMode(){
        utils.editByLabel("Priority Code");
    }
    @Test(groups = {"Validations & Business Rules", "Dropdown Validations"}, dataProvider = "statusSubstatusData", dataProviderClass = ValidationDataProviders.class)
    public void verifySubStatusListFiltersCorrectlyForEachStatus(String status) throws InterruptedException {
        utils.selectFromComboBoxByLabel("Status", status);
        List<String> actualOptions =
                utils.getValuesFromDropdownByLabel("Sub-Status");
        List<String> expectedOptions =
                STATUS_TO_SUBSTATUS.get(status);
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
                "Sub-Status mismatch for Status: " + status +
                        "\nMissing: " + missing +
                        "\nExtra: " + extra
        );
        //  PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
    }

  *//*  @BeforeMethod
    public void edit() {
        utils.editByLabel("Priority Code");
    }*//*


    @Test(groups = {"Validations & Business Rules", "Dropdown Validations"}, dataProvider = "typeSubtypeData", dataProviderClass = ValidationDataProviders.class)
    public void verifySubTypeListFiltersCorrectlyForEachType(String type) throws InterruptedException {
        //  utils.editByLabel("Priority Code");
        utils.selectFromComboBoxByLabel("Type", type);
        List<String> actualOptions =
                utils.getValuesFromDropdownByLabel("Subtype");

        List<String> expectedOptions =
                TYPE_TO_SUBTYPE.get(type);
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
                "SubType mismatch for Type: " + type +
                        "\nMissing: " + missing +
                        "\nExtra: " + extra
        );

    }

    @AfterMethod(onlyForGroups = "Dropdown Validations" )
    public void navigateBack() {
        PageFactory.getSpaDetailsPage(getDriver(), getUtils()).clickCancel();
        PageFactory.getHomePage(getDriver(), getUtils()).goToSpasWaiversPage();
        utils.clickElement(By.xpath("//lightning-datatable//tbody/tr/td[2]/ancestor::tr/th//a[@title=\"" + spaPackage.getPackageId() + "\"]"));
    }*/
}
