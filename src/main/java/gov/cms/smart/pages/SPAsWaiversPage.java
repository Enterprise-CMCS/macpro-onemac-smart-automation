package gov.cms.smart.pages;

import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class SPAsWaiversPage {

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

    private final WebDriver driver;
    private final UIElementUtils utils;

    public SPAsWaiversPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }


    public HomePage goToHomePage() {
        utils.clickElement(HOME_PAGE);
        utils.waitForVisibility(ALERT);


        return PageFactory.getHomePage(driver, utils);
    }

    public NewSPAPage clickNew() {
        utils.clickElement(NEW_BUTTON);
        return PageFactory.getNewSPAPage(driver, utils);
    }

    public SpaDetailsPage openExistingRecord(SpaPackage spaPackage) {
        driver.navigate().refresh();
        utils.clearInput(SEARCH_INPUT);
        utils.sendKeys(SEARCH_INPUT, spaPackage.getPackageId());
        utils.sendKeys(SEARCH_INPUT, Keys.ENTER);
        utils.openRecord(spaPackage.getPackageId());
        return PageFactory.getSpaDetailsPage(driver, utils);
    }


    public SPAsWaiversPage openRecordFromAllRecordsView(SpaPackage spa) throws InterruptedException {
        utils.clickElement(RECORDS_HEADER);
        utils.selectDropdownBy(HEADER_DROPDOWN, "All Records");
        utils.sendKeys(SEARCH_INPUT, spa.getPackageId());
        utils.sendKeys(SEARCH_INPUT, Keys.ENTER);
        utils.openRecord(spa.getPackageId());
        logger.info("Opened SPA: {}", spa.getPackageId());

        return this;
    }


    public void assignSRT(String srtName, String assignmentNotes) {
        utils.clickElement(SRT_TAB);
        utils.sendKeys(SRT_SEARCH_INPUT, srtName);
        utils.waitForVisibility(By.xpath("//label[text()=\"Division\"]"));
        utils.clickElement(SRT_SEARCH_BTN);
        utils.clickElement(SRT_CHECKBOX);
        utils.clickElement(SRT_ASSIGN_BTN);
        utils.sendKeysToTextAreaByLabel("Assignment Notes", assignmentNotes);
        utils.clickElement(SRT_SAVE_ASSIGNMENT);
        logger.info("Successfully Assigned SRT.");
    }

    public void fillSRT1Details(String status) throws InterruptedException {
        utils.clickElement(SRT_TAB);
        utils.clickElement(SRT1_CELL);
        utils.editByLabel("Concurrence");
        utils.selectFromComboBoxByLabel("Status", status);
        utils.selectFromComboBoxByLabel("RAI Needed", "No");
        utils.sendKeysToTextAreaByLabel("SRT Notes", "Automation Test script for when an RAI isn't needed and SRT recommends approval");
        utils.clickElement(SAVE);
    }

    public void fillSRT2DetailsRequestRAI(String status) throws InterruptedException, IOException {
        utils.clickElement(SRT_TAB);
        utils.clickElement(SRT2_CELL);
        utils.editByLabel("Concurrence");
        utils.selectFromComboBoxByLabel("Status", status);
        utils.selectFromComboBoxByLabel("RAI Needed", "Yes");
        utils.sendKeysToTextAreaByLabel("RAI Questions", "Need more information can't approved");
        utils.selectFromComboBoxByLabel("RAI Questions Completed", "Yes");
        utils.sendKeysToTextAreaByLabel("SRT Notes", "More information is needed");
        utils.clickElement(SAVE);
    }

    public void requestRAI() {
        utils.clickElement(CREATE_RAI);
        utils.sendKeys(RAI_TEXT, "test");
        utils.clickElement(SAVE_RAI);
    }
}

