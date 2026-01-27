package gov.cms.smart.pages;

import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.PageFactory;
import gov.cms.smart.utils.SpaGenerator;
import gov.cms.smart.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SPAPage {

    private WebDriver driver;
    private UIElementUtils utils;
    private static final By SPA_ID = By.xpath("//label[contains(text(),'ID Number')]/parent::div/div/input");
    private static final By INITIAL_SUBMISSION_DATE = By.xpath("//label[contains(text(),'Initial Submission Date')]/parent::div/div/input");
    private static final By STATE_COMBO_BOX = By.xpath("//lightning-base-combobox/div");
    private static final By COMBO_BOX_ITEMS = By.xpath("//lightning-base-combobox-item/span/span");
    private static final By NEW_SUBMISSION = By.xpath("//a[@title=\"New\"]/ancestor::ul/..");
    private static final By NEXT_BUTTON = By.xpath("//button[span[text()='Next']]");
    private static final String AUTHORITY = "//span[text()=\"placeholder\"]/ancestor::label//input";
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By ID_NUMBER_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_ID_Number__c\"]/span");
    private static final By INITIAL_SUBMISSION_DATE_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]/span");
    private static final By STATE_FIELD_VALIDATION = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]/span");
    private static final By STATE_FIELD_VALIDATION_DIV = By.xpath("//div[@data-name=\"SMART_CMCS_Initial_Submission_Date__c\"]");


    public SPAPage(WebDriver driver, UIElementUtils utils) {
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

    public void enterSPADetails(String state) {
        clickNewSubmission();
        SpaPackage spa = SpaGenerator.createSpa(state, "Medicaid SPA");
        selectAuthority(spa.getAuthority());
        clickNextButton();
        utils.sendKeys(SPA_ID, spa.getPackageId());
        utils.sendKeys(INITIAL_SUBMISSION_DATE, utils.getTodayDateFormatted());
        utils.clickElement(STATE_COMBO_BOX);
        utils.selectDropdownBy(COMBO_BOX_ITEMS, utils.getStateFullName(state));
        //utils.clickElement(SAVE);
        //driver.findElement(By.cssSelector("button[title=\"Cancel and close\"]")).click();
    }

    public void modelTest() throws InterruptedException {
        utils.sleepFor(2000);
        //  PriorityInfoSection priorityInfoSection = new PriorityInfoSection();
        driver.get("https://cmsapps5--cmcsmcqa.sandbox.lightning.force.com/lightning/r/SMART_CMCS_SPA_Waiver__c/a0vSL000007uFRmYAM/view");
        utils.sleepFor(2000);
        IdentifyingInfo info = PageFactory.getSpaDetailsPage(driver, utils).readIdentifyingInfo();
        System.out.println(info.getIdNumber());
        System.out.println(info.getAuthority());
        System.out.println(info.getState());
        //utils.clickElement(By.xpath("//span[text()=\"Coding After Initial Assessment\"]/../following-sibling::div//button"));
        // driver.findElement(By.xpath("//span[text()=\"Coding After Initial Assessment\"]/../following-sibling::div//button")).click();
        //utils.isVisible(By.xpath("//button[text()=\"Save\"]"));
        // utils.clickElement(By.xpath("//label[text()=\"Coding After Initial Assessment\"]//../parent::div/child::div/lightning-base-combobox/div"));
        //utils.selectDropdownBy(COMBO_BOX_ITEMS, "Same");
        // utils.selectFromComboBox("Coding After Initial Assessment","Up Coded");
       /* utils.sleepFor(1000);
        driver.get("https://cmsapps5--cmcsmcqa.sandbox.lightning.force.com/lightning/r/SMART_CMCS_SPA_Waiver__c/a0vSL000007uFRmYAM/view");
        utils.sleepFor(1000);
        System.out.println(utils.isVisible(By.xpath("//button[@title=\"Edit \uFEFFID Number\"]")));
         utils.selectFromComboBox("Priority Code","P1 - Escalated Review");*/
        //   clickSave();

        //   driver.findElement()).click();
        //   priorityInfoSection.setPriorityCode();

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

    public boolean isDateFormatErrorDisplayed() throws InterruptedException {
        return utils.elementContainsText(driver, STATE_FIELD_VALIDATION_DIV, "Your entry does not match the allowed format 12/31/2024.");

    }

}
