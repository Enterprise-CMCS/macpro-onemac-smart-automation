package gov.cms.smart.pages;

import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.data.SpaGenerator;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NewSPAPage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By SPA_ID = By.xpath("//label[contains(text(),'ID Number')]/parent::div/div/input");
    private static final By INITIAL_SUBMISSION_DATE = By.xpath("//label[contains(text(),'Initial Submission Date')]/parent::div/div/input");
    private static final By STATE_COMBO_BOX = By.xpath("//lightning-base-combobox/div");
    private static final By COMBO_BOX_ITEMS = By.xpath("//lightning-base-combobox-item/span/span");
    private static final By NEW_SUBMISSION = By.xpath("//lst-list-view-manager-header//a[@title=\"New\"]");
    private static final By NEXT_BUTTON = By.xpath("//button[span[text()='Next']]");
    private static final String AUTHORITY = "//span[text()=\"placeholder\"]/ancestor::label//input";
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By ID_NUMBER_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_ID_Number__c\"]/span");
    private static final By INITIAL_SUBMISSION_DATE_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]/span");
    private static final By STATE_FIELD_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]/span");
    private static final By STATE_FIELD_VALIDATION_DIV = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div[data-key=\"success\"]");

    public NewSPAPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void clickNextButton() {
        utils.clickElement(NEXT_BUTTON);
    }

    public void clickNewSubmission() {
        utils.clickElement(NEW_SUBMISSION);
    }

    public void selectAuthority(String text) {
        String authorityXpath = AUTHORITY.replace("placeholder", text);
        utils.javaScriptClicker(By.xpath(authorityXpath));
    }

    public void createSPA(String state) {
        SpaPackage spa = SpaGenerator.createSpa(state, "Medicaid SPA");
        selectAuthority(spa.getAuthority());
        clickNextButton();
        utils.sendKeys(SPA_ID, spa.getPackageId());
        utils.sendKeys(INITIAL_SUBMISSION_DATE, utils.getTodayDateFormatted());
        utils.clickElement(STATE_COMBO_BOX);
        utils.selectDropdownBy(COMBO_BOX_ITEMS, utils.getStateFullName(state));
        utils.clickElement(SAVE);
        utils.isVisible(SUCCESS_MESSAGE);
        System.out.println(spa.getPackageId());
    }


    public void selectPriorityCode() {

    }

    public void enterSpaId(String spa) {
        utils.sendKeys(SPA_ID, spa);
    }

    public void enterInitialSubmissionDate(String date) {
        utils.sendKeys(INITIAL_SUBMISSION_DATE, date);
    }

    public void navigateToMedicaidSPAForm() {
        clickNewSubmission();
        selectAuthority("Medicaid SPA");
        clickNextButton();
    }

    public void clickSave() {
        utils.clickElement(SAVE);
    }

    public boolean isIDNumberErrorDisplayed() {
        return utils.isVisible(ID_NUMBER_VALIDATION);
    }

    public boolean isInitialSubmissionDateErrorDisplayed() {
        return utils.isVisible(INITIAL_SUBMISSION_DATE_VALIDATION);
    }

    public boolean isStateErrorDisplayed() {
        return utils.isVisible(STATE_FIELD_VALIDATION);
    }

    /*public boolean isDateFormatErrorDisplayed() throws InterruptedException {
        return utils.elementContainsText(driver, STATE_FIELD_VALIDATION_DIV, "Your entry does not match the allowed format 12/31/2024.");

    }*/

}
