package gov.cms.smart.pages;

import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsTab {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div[data-key=\"success\"]");
    private static final By ASSIGN_TO_ME = By.xpath("//button[@name=\"SMART_CMCS_SPA_Waiver__c.SMART_Assign_to_Me\"]");
    private static final By ASSIGNMENT_SUCCESS = By.xpath("//p[text()=\"You have been assigned as CPOC and now have Edit access to this record.\"]");
    private static final By FINISH_BUTTON = By.xpath("//button[text()=\"Finish\"]");
    private static final By CPOC_TEXT_VAL = By.xpath("//span[text()=\"CMS Point of Contact (CPOC)\"]/../following-sibling::div//records-hoverable-link//span//span//span");
    private static final By CANCEL = By.xpath("//button[text()=\"Cancel\"]");
    private static final By DISCARD_CHANGES = By.xpath("//button[text()=\"Discard Changes\"]");
    private static final By PRIORITY_INFO = By.xpath("//span[text()=\"Priority Information\"]");
    private static final By PRIORITY_CODE = By.xpath("//span[text()=\"Priority Information\"]/ancestor::flexipage-field-section2//span[text()=\"Priority Code\"]");
    private static final By CODING_ASSESSMENT = By.xpath("//span[text()=\"Priority Information\"]/ancestor::flexipage-field-section2//span[text()=\"Coding After Initial Assessment\"]");
    private static final By PRIORITY_COMMENTS = By.xpath("//span[text()=\"Priority Information\"]/ancestor::flexipage-field-section2//span[text()=\"Priority Comments Memo\"]");
    private static final By CODING_CHANGE_DATE = By.xpath("//span[text()=\"Priority Information\"]/ancestor::flexipage-field-section2//span[text()=\"Date Of Coding Change\"]");
    private static final By TYPE = By.xpath("//span[text()=\"Identifying\"]/ancestor::flexipage-field-section2//span[text()=\"Type\"]");
    public static final By SUB_TYPE = By.xpath("//span[text()=\"Identifying Information\"]/ancestor::flexipage-field-section2//span[text()=\"Subtype\"]");
    public static final By SUB_TYPE_SI = By.xpath("//span[text()=\"Submission Information\"]/ancestor::flexipage-field-section2//span[text()=\"Subtype\"]");
    private static final By TYPE_SI = By.xpath("//span[text()=\"Submission Information\"]/ancestor::flexipage-field-section2//span[text()=\"Type\"]");
    public static final By MILESTONES_SECTION = By.xpath("//span[text()=\"Milestones\"]");
    public static final By SUBMISSION_INFORMATION_SECTION = By.xpath("//span[text()=\"Submission Information\"]");
    private static final By SUB_STATUS = By.xpath("//span[text()=\"Completion Status\"]/ancestor::flexipage-field-section2//span[text()=\"Sub-Status\"]");
    private static final By HOME_PAGE = By.xpath("//span[text()=\"Home\"]/parent::a");
    public static final By IDENTIFYING_INFORMATION = By.xpath("//span[text()=\"Identifying Information\"]");
    private static final By NEW = By.xpath("//button[text()=\"New\"]");
    public static final By WARNING_BANNER = By.xpath("//c-smart-notification-utility/div//h2[text()=\"WARNING: COMPLETION REVIEW DATE SHOULD BE ENTERED WITHIN 5 DAYS OF INITIAL SUBMISSION DATE\"]");
    public static final By DAYS_ON_ACTIVE_CLOCK_HEADER = By.xpath("//span[normalize-space()='Days on Active Clock']/ancestor::record_flexipage-record-field");
    public static final By DAYS_ON_ACTIVE_CLOCK_VALUE = By.xpath("//span[normalize-space()='Days on Active Clock']/ancestor::record_flexipage-record-field//lightning-formatted-number");
    public static final By REMAINING_DAYS = By.xpath("//span[normalize-space()='Days Remaining']/ancestor::record_flexipage-record-field//lightning-formatted-number");
    public static final By FIELD = By.xpath("//span[text()=\"Authority\"]/ancestor::flexipage-field[@slot=\"secondaryFields\"]//div[text()=\"This field is calculated upon save\"]");

    //private static final By SRT_SEARCH_INPUT = By.xpath("//input[@placeholder=\"Search Contacts...\"]");
    private static final By SRT_SEARCH_INPUT = By.xpath("//input[@placeholder=\"Enter contact name or email...\"]");
    private static final Logger logger = LogManager.getLogger();

    public DetailsTab(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void waitForPriorityInfoDataToSave(String priorityCode, String codingAssessment, String priorityComments, String dateOfCodingChange) {
        utils.waitForFieldTextToBe("Priority Information", "Priority Code", priorityCode);
        utils.waitForFieldTextToBe("Priority Information", "Coding After Initial Assessment", codingAssessment);
        utils.waitForFieldTextToBe("Priority Information", "Priority Comments Memo", priorityComments);
        utils.waitForFieldTextToBe("Priority Information", "Date Of Coding Change", dateOfCodingChange);
    }

    public boolean isTypeAndSubTypeVisibleUnderSubmissionInfo() {
        utils.scrollToElement(MILESTONES_SECTION);
        return utils.isVisible(SUB_TYPE_SI) && utils.isVisible(TYPE_SI);
    }

    public HomePage goToHomePage() {
        utils.clickElement(HOME_PAGE);
        return PageFactory.getHomePage(driver, utils);
    }

    public boolean isSubStatusDisplayedUnderCompletionStatus() {
        return utils.isVisible(SUB_STATUS);
    }

    public boolean isTypeVisibleUnderIdentifyingInfo() {
        return utils.isVisible(TYPE);
    }

    public boolean isSubTypeVisibleUnderIdentifyingInfo() {
        return utils.isVisible(SUB_TYPE);
    }


    public boolean areFieldsGroupedCorrectly() {
        return utils.isVisible(PRIORITY_CODE) && utils.isVisible(CODING_ASSESSMENT) && utils.isVisible(PRIORITY_COMMENTS) && utils.isVisible(CODING_CHANGE_DATE);
    }

    public IdentifyingInfo readIdentifyingInfo() {
        IdentifyingInfo info = new IdentifyingInfo();
        utils.scrollToElement(IDENTIFYING_INFORMATION);
        info.setIdNumber(utils.getFieldTextByLabel("ID Number"));
        info.setAuthority(utils.getFieldTextByLabel("Authority"));
        info.setState(utils.getFieldTextByLabel("State"));
        return info;
    }

    public boolean isPriorityInfoPresent() {
        return utils.isVisible(PRIORITY_INFO);
    }

    public void clickCancel() {
        utils.clickElement(CANCEL);
        utils.waitForInvisibility(CANCEL);
    }

    public void discardChanges() {
        utils.clickElement(DISCARD_CHANGES);
    }

    public boolean validateSubtypes(String typeLabel, String typeValue, String subTypeLabel) throws InterruptedException {
        // Select the type in the first dropdown
        utils.editByLabel("Priority Code");
        utils.selectFromComboBoxByLabel(typeLabel, typeValue);
        // Get actual options from the second dropdown
        List<String> actualOptions = utils.getValuesFromDropdownByLabel(subTypeLabel);
        Map<String, List<String>> typeToSubtypes = new HashMap<>();
        typeToSubtypes.put("Eligibility", Arrays.asList("--None--", "Eligibiity Process", "MAGI Eligibility & Methods", "Citizenship Documentation", "Medicaid Expansion", "Nonfinancial Eligibility", "Premiums", "Other"));
        typeToSubtypes.put("Benefits", Arrays.asList("--None--", "Benchmark", "Change Benefit Limit", "New Benefit", "Provider Qualifications", "Other"));
        typeToSubtypes.put("Payment", Arrays.asList("--None--", "Rate Change", "Process Change", "Other"));
        // Get expected options from the map
        List<String> expectedOptions = typeToSubtypes.get(typeValue);
        // Compare
        if (actualOptions.containsAll(expectedOptions) && actualOptions.size() == expectedOptions.size()) {
            System.out.println("Validation passed for type: " + typeValue);
            return true;
        } else {
            System.out.println("Validation failed for type: " + typeValue);
            System.out.println("Expected: " + expectedOptions);
            System.out.println("Actual: " + actualOptions);
            return false;
        }
    }

    public boolean validateCompletionStatus(String typeLabel, String typeValue, String subTypeLabel) throws InterruptedException {
        // Select the type in the first dropdown
        utils.editByLabel("Priority Code");
        utils.selectFromComboBoxByLabel(typeLabel, typeValue);
        // Get actual options from the second dropdown
        List<String> actualOptions = utils.getValuesFromDropdownByLabel(subTypeLabel);
        Map<String, List<String>> typeToSubtypes = new HashMap<>();
        typeToSubtypes.put("Closed", Arrays.asList("--None--", "Approved", "Disapproved", "Withdrawn"));
        typeToSubtypes.put("First Clock - Under Review", Arrays.asList("--None--", "Intake Needed", "Pending Concurrence", "Pending Approval", "Pending Clearance"));
        typeToSubtypes.put("Second Clock - Under Review", Arrays.asList("--None--", "Pending Concurrence", "Pending Approval", "Pending Clearance"));
        typeToSubtypes.put("RAI", Arrays.asList("--None--", "RAI Issued", "RAI Withdraw Requested"));
        // Get expected options from the map
        List<String> expectedOptions = typeToSubtypes.get(typeValue);
        // Compare
        if (actualOptions.containsAll(expectedOptions) && actualOptions.size() == expectedOptions.size()) {
            System.out.println("Validation passed for type: " + typeValue);
            return true;
        } else {
            System.out.println("Validation failed for type: " + typeValue);
            System.out.println("Expected: " + expectedOptions);
            System.out.println("Actual: " + actualOptions);
            return false;
        }
    }

    public PriorityInfo fillPriorityInfo(PriorityCode priorityCode, CodingAssessment codingAssessment) throws InterruptedException {
        PriorityInfo priorityInfo = new PriorityInfo();
        priorityInfo.setPriorityComments("Test");
        utils.editByLabel("Priority Code");
        utils.selectFromComboBoxByLabel("Priority Code", priorityCode.getValue());
        priorityInfo.setPriorityCode(priorityCode);
        utils.selectFromComboBoxByLabel("Coding After Initial Assessment", codingAssessment.getValue());
        utils.clearTextAreaByLabel("Priority Comments Memo");
        utils.sendKeysToTextAreaByLabel("Priority Comments Memo", "Test");
        utils.clearInputByLabel("Date Of Coding Change");
        utils.sendKeysToInputByLabel("Date Of Coding Change", utils.getTodayDateFormatted());
        priorityInfo.setDateOfCodingChange(utils.getTodayDateFormatted());
        priorityInfo.setCodingAssessment(codingAssessment);
        utils.clickElement(SAVE);
        waitForPriorityInfoDataToSave(priorityCode.getValue(), codingAssessment.getValue(), "Test", utils.getTodayDateFormatted());
        //  utils.waitForInvisibility(SAVE);
        return priorityInfo;
    }

    public PriorityInfo readPriorityInfo() {
        PriorityInfo priorityInfo = new PriorityInfo();
        String priorityCode = utils.getFieldTextByLabel("Priority Code");
        priorityInfo.setPriorityCode(PriorityCode.fromUiValue(priorityCode));
        priorityInfo.setPriorityComments(utils.getFieldTextByLabel("Priority Comments Memo"));
        priorityInfo.setDateOfCodingChange(utils.getFieldTextByLabel("Date Of Coding Change"));
        String codingAssessment = utils.getFieldTextByLabel("Coding After Initial Assessment");
        priorityInfo.setCodingAssessment(CodingAssessment.fromUiValue(codingAssessment));
        return priorityInfo;
    }


    public PlanInfo fillPlanInfo(String subject, String description) {
        PlanInfo planInfo = new PlanInfo();
        utils.editByLabel("Subject");
        planInfo.setSubject(utils.sendKeysToInputByLabel("Subject", subject));
        planInfo.setDescription(utils.sendKeysToTextAreaByLabel("Description", description));
        utils.clickElement(SAVE);
        utils.waitForInvisibility(SAVE);
        return planInfo;
    }

    public void save() {
        utils.clickElement(SAVE);
    }

    public PlanInfo readPlanInfo() {
        PlanInfo planInfo = new PlanInfo();
        planInfo.setSubject(utils.getFieldTextByLabel("Subject"));
        planInfo.setDescription(utils.getFieldTextByLabel("Description"));
        return planInfo;
    }

    public boolean assignToMe(SpaPackage spaPackage) {
        logger.info("Assigning SPA: {}...", spaPackage.getPackageId());
        utils.clickElement(ASSIGN_TO_ME);
        utils.waitForVisibility(ASSIGNMENT_SUCCESS);
        utils.clickElement(FINISH_BUTTON);
        utils.waitForTextToBePresent(CPOC_TEXT_VAL, "CPOC4 AutomationCPOC");
        logger.info("Successfully Assigned SPA: {} To CPOC4 AutomationCPOC", spaPackage.getPackageId());
        return true;
    }

    public void srtAssignment() {
       /* utils.clickElement(NEW);
        utils.sendKeys(SRT_SEARCH_INPUT, "SRT Test Automation User");
        By xpath = By.xpath("//ul[@aria-label=\"Search Results\"]/li/lightning-base-combobox-item//lightning-base-combobox-formatted-text[@title=\"SRT Test Automation User\"]/ancestor::li");
        utils.clickElement(xpath);
        utils.clickElement(SAVE);*/


    /*    utils.waitForVisibility(By.xpath("//label[text()=\"Division\"]"));
        utils.clickElement(SRT_SEARCH_BTN);
        utils.clickElement(SRT_CHECKBOX);
        utils.clickElement(SRT_ASSIGN_BTN);
        utils.sendKeysToTextAreaByLabel("Assignment Notes", assignmentNotes);
        utils.clickElement(SRT_SAVE_ASSIGNMENT);
        logger.info("Successfully Assigned SRT.");*/

    }

    public void assignRecord() {
        utils.clickElement(ASSIGN_TO_ME);
        utils.waitForVisibility(ASSIGNMENT_SUCCESS);
        utils.clickElement(FINISH_BUTTON);
    }

    // ===== SERVICE & STATUS =====
    //  private By serviceType = By.id("serviceType");
    //  private By status = By.id("status");

/*    public ServiceAndStatusInfo readServiceAndStatus() {
        ServiceAndStatusInfo s = new ServiceAndStatusInfo();
        s.setServiceType(utils.getText(serviceType));
        s.setStatus(utils.getText(status));
        return s;
    }

    // ===== KEY DATES =====
    private By submissionDate = By.id("submissionDate");

    public KeyDatesInfo readKeyDates() {
        KeyDatesInfo k = new KeyDatesInfo();
        k.setSubmissionDate(utils.getText(submissionDate));
        return k;
    }*/
}
