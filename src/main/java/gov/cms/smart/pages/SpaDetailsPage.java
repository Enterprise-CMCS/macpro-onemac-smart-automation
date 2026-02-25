package gov.cms.smart.pages;

import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaDetailsPage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div[data-key=\"success\"]");
    private static final By ASSIGN_TO_ME = By.xpath("//button[@name=\"SMART_CMCS_SPA_Waiver__c.SMART_Assign_to_Me\"]");
    private static final By ASSIGNMENT_SUCCESS = By.xpath("//p[text()=\"You have been assigned as CPOC and now have Edit access to this record.\"]");
    private static final By FINISH_BUTTON = By.xpath("//button[text()=\"Finish\"]");
    private static final By CPOC_TEXT_VAL = By.xpath("//span[text()=\"CMS Point of Contact (CPOC)\"]/../following-sibling::div//records-hoverable-link//span//span//span");
    private static final By CANCEL = By.xpath("//button[text()=\"Cancel\"]");
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
    private static final By SUB_STATUS = By.xpath("//span[text()=\"Completion Status\"]/ancestor::flexipage-field-section2//span[text()=\"Sub-Status\"]");
    private static final By HOME_PAGE = By.xpath("//span[text()=\"Home\"]/parent::a");
    private static final By IDENTIFYING_INFORMATION = By.xpath("//span[text()=\"Identifying Information\"]");

    public SpaDetailsPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
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

    public void details() {
        Map<String, List<String>> typeToSubtypes = new HashMap<>();
        typeToSubtypes.put("Eligibility", Arrays.asList("eligibility process", "MAGI", "citizenship document"));
        typeToSubtypes.put("Enrollment", Arrays.asList("open enrollment", "special enrollment"));
        typeToSubtypes.put("Verification", Arrays.asList("income verification", "identity verification"));
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
        Thread.sleep(1000);
        utils.waitForInvisibility(SAVE);
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

    public By assignToMe() {
        utils.clickElement(ASSIGN_TO_ME);
        utils.waitForVisibility(ASSIGNMENT_SUCCESS);
        utils.clickElement(FINISH_BUTTON);
        return CPOC_TEXT_VAL;
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
