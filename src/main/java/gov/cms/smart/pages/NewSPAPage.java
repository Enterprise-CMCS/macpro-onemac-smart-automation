package gov.cms.smart.pages;

import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.data.SpaGenerator;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final By ID_NUMBER_VALIDATION = By.xpath("//div[@data-name=\"Name\"]/span");
    private static final By INITIAL_SUBMISSION_DATE_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]/span");
    private static final By STATE_FIELD_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]/span");
    private static final By STATE_FIELD_VALIDATION_DIV = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div[data-key=\"success\"]");
    private static final By CANCEL = By.xpath("//button[text()='Cancel']");
    private static final By CANCEL_CLOSE = By.cssSelector("button[title=\"Cancel and close\"]");

    private static final By MODAL = By.cssSelector("div[class=\"isModal inlinePanel oneRecordActionWrapper\"]");
    private static final Logger logger = LogManager.getLogger();

    public NewSPAPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void clickNextButton() {
        utils.clickElement(NEXT_BUTTON);
    }

    public void clickCancelButton() {
        utils.clickElement(CANCEL);
        utils.waitForInvisibility(MODAL);

    }

    public void clickCancelAndClose() {
        utils.clickElement(CANCEL_CLOSE);
        utils.waitForInvisibility(MODAL);
    }

    public void clickNewSubmission() {
        utils.clickElement(NEW_SUBMISSION);
    }

    public void selectAuthority(String text) {
        String authorityXpath = AUTHORITY.replace("placeholder", text);
        utils.javaScriptClicker(By.xpath(authorityXpath));
    }

    private SpaPackage createSpaInternal(String state, String authority)
            throws InterruptedException {

        logger.info("Creating {}...", authority);

        SpaPackage spa = SpaGenerator.createSpa(state, authority);

        selectAuthority(spa.getAuthority());
        clickNextButton();

        utils.sendKeys(SPA_ID, spa.getPackageId());
        utils.clickElement(STATE_COMBO_BOX);
        utils.selectDropdownBy(COMBO_BOX_ITEMS,
                utils.getStateFullName(state));

        utils.sendKeys(INITIAL_SUBMISSION_DATE,
                utils.getTodayDateFormatted());

        utils.clickElement(SAVE);

        utils.isVisible(SUCCESS_MESSAGE);

        logger.info("{}: {} has been successfully created.", authority, spa.getPackageId());

        return spa;
    }

    public boolean createSPA(String state, String authority)
            throws InterruptedException {

        createSpaInternal(state, authority);
        return utils.isVisible(SUCCESS_MESSAGE);
    }

    public SpaPackage createSPARecord(String state, String authority)
            throws InterruptedException {
        return createSpaInternal(state, authority);
    }


    public void selectPriorityCode() {

    }

    public void enterSpaId(String spa) {
        utils.sendKeys(SPA_ID, spa);
    }

    public void enterInitialSubmissionDate(String date) {
        utils.sendKeys(INITIAL_SUBMISSION_DATE, date);
    }

    public NewSPAPage navigateToMedicaidSPAForm() {
        clickNewSubmission();
        selectAuthority("Medicaid SPA");
        clickNextButton();
        return this;
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

    public boolean isDateFormatErrorDisplayed() throws InterruptedException {
        return utils.elementContainsText(driver, STATE_FIELD_VALIDATION_DIV, "Your entry does not match the allowed format 12/31/2024.");

    }

}
