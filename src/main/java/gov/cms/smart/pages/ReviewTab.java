package gov.cms.smart.pages;

import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewTab {
    private static final Logger logger = LogManager.getLogger();
    private static final By NEW_BUTTON = By.xpath("//div[contains(@class,\"actionsWrapper\")]");
    private static final By SEARCH_INPUT = By.xpath("//lst-list-view-manager-settings-menu/parent::div/..//force-list-view-manager-search-bar//input");
    private static final By RECORDS_HEADER = By.xpath("//h1[@class=\"slds-page-header__name-title slds-var-p-right_xx-small\"]/..");
    private static final By HEADER_DROPDOWN = By.xpath("//lightning-base-combobox-item/span/span");
    private static final By SRT_TAB = By.xpath("//a[text()=\"SRT\"]/..");
    private static final By SRT_SEARCH_INPUT = By.xpath("//input[@placeholder=\"Enter contact name or email...\"]");
    private static final By SRT_SEARCH_BTN = By.xpath("//button[text()=\"Search\"]");
    private static final By SRT_CHECKBOX = By.xpath("//lightning-primitive-cell-checkbox");
    private static final By SRT_ASSIGN_BTN = By.xpath("//button[text()=\"Assign\"]");
    private static final By SRT_SAVE_ASSIGNMENT = By.xpath("//button[text()=\"Save Assignments\"]");
    private static final By SRT1_CELL = By.xpath("//span[@title=\"Submission Review Team Number\"]/ancestor::table//tbody/tr[1]//th/lightning-primitive-cell-factory");
    private static final By SRT2_CELL = By.xpath("//span[@title=\"Submission Review Team Number\"]/ancestor::table//tbody/tr[2]//th/lightning-primitive-cell-factory");
    private static final By HOME_PAGE = By.xpath("//span[text()=\"Home\"]/parent::a");
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By CREATE_RAI = By.xpath("//button[@name=\"SMART_CMCS_SPA_Waiver__c.Create_RAI\"]");
    private static final By RAI_TEXT = By.xpath("//div[@role=\"textbox\"]/p");
    private static final By SAVE_RAI = By.xpath("//footer/button/span[text()=\"Save\"]");
    private static final By ALERT = By.xpath("//strong[text()=\"ALERT!\"]");
    private static final By REVIEW_TAB = By.xpath("//a[text()=\"Review\"]/..");
    public static final By RAI_INFO_SECTION = By.xpath("//strong[text()=\"RAI Information\"]");
    public static final By SRT_QUESTIONS_ARROW = By.xpath("//th[@aria-label=\"RAI Needed\"]/ancestor::table//tbody/tr/td[5]//button");
    public static final By DELETE_RAI_QUESTION = By.cssSelector("a[title=\"Delete\"]");
    public static final By EDIT_RAI_QUESTION = By.cssSelector("a[title=\"Edit\"]");
    public static final By CONFIRM_SRT_DELETION = By.cssSelector("button[title=\"Delete\"]");
    public static final By SRT_DELETED_MESSAGE = By.xpath("//span[contains(text(),'Submission Review Team was deleted.')]");
    public static final By RAI_QUESTIONS_SAVED = By.xpath("//span[contains(.,\"Submission Review Team\") and contains(.,\"was saved.\")]");
    public static final By SAVE_BUTTON = By.xpath("//button[text()=\"Save\"]");

    private final WebDriver driver;
    private final UIElementUtils utils;

    public ReviewTab(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public boolean validateGroupAndDivisions(String groupLabel, String groupValue, String divisionLabel) throws InterruptedException {
        // Select the type in the first dropdown
        //  utils.editByLabel("Priority Code");
        utils.selectFromComboBoxByLabel(groupLabel, groupValue);
        // Get actual options from the second dropdown
        List<String> actualOptions = utils.getValuesFromDropdownByLabel(divisionLabel);
        System.out.println(actualOptions);
        Map<String, List<String>> groupToDivision = new HashMap<>();
        groupToDivision.put("CAHPG", Arrays.asList("--None--", "DEPO", "DMEP", "DQHO", "DSCP", "DTA", "CAHPG - Office of Group Director"));
        //typeToSubtypes.put("First Clock - Under Review", Arrays.asList("--None--", "Intake Needed", "Pending Concurrence", "Pending Approval", "Pending Clearance"));
        //typeToSubtypes.put("Second Clock - Under Review", Arrays.asList("--None--", "Pending Concurrence", "Pending Approval", "Pending Clearance"));
        // typeToSubtypes.put("RAI", Arrays.asList("--None--", "RAI Issued", "RAI Withdraw Requested"));
        // Get expected options from the map
        List<String> expectedOptions = groupToDivision.get("CAHPG");
        System.out.println(expectedOptions);
        // Compare
        if (actualOptions.containsAll(expectedOptions) && actualOptions.size() == expectedOptions.size()) {
            System.out.println("Validation passed for type: " + divisionLabel);
            return true;
        } else {
            System.out.println("Validation failed for type: " + divisionLabel);
            System.out.println("Expected: " + expectedOptions);
            System.out.println("Actual: " + actualOptions);
            return false;
        }
    }

    public void assignSRT() {
        utils.sendKeys(SRT_SEARCH_INPUT, "SRT Test Automation User");
        utils.waitForVisibility(By.xpath("//label[text()=\"Division\"]"));
        utils.clickElement(SRT_SEARCH_BTN);
        utils.clickElement(SRT_CHECKBOX);
        utils.clickElement(SRT_ASSIGN_BTN);
        utils.sendKeysToTextAreaByLabel("Review Assignment", "This is an automation test script to assign SRT.");
        //utils.sendKeysToTextAreaByLabel("Assignment Notes", assignmentNotes);
        utils.clickElement(SRT_SAVE_ASSIGNMENT);
        logger.info("Successfully Assigned SRT.");
    }

}
