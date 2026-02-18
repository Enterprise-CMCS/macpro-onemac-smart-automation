package gov.cms.smart.pages;

import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PlanInfo;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SpaDetailsPage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div[data-key=\"success\"]");
    private static final By ASSIGN_TO_ME = By.xpath("//button[@name=\"SMART_CMCS_SPA_Waiver__c.SMART_Assign_to_Me\"]");
    private static final By ASSIGNMENT_SUCCESS = By.xpath("//p[text()=\"You have been assigned as CPOC and now have Edit access to this record.\"]");
    private static final By FINISH_BUTTON = By.xpath("//button[text()=\"Finish\"]");
    private static final By TEXT = By.xpath("//p[text()=\"CMS Point of Contact (CPOC)\"]/following-sibling::p//records-hoverable-link//a/span/slot/span/slot/span");
    private static final By CANCEL = By.xpath("//button[text()=\"Cancel\"]");

    public SpaDetailsPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public IdentifyingInfo readIdentifyingInfo() {
        IdentifyingInfo info = new IdentifyingInfo();
        info.setIdNumber(utils.getFieldTextByLabel("ID Number"));
        info.setAuthority(utils.getFieldTextByLabel("Authority"));
        info.setState(utils.getFieldTextByLabel("State"));
        return info;
    }

    public void clickCancel() {
        utils.clickElement(CANCEL);
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
        return TEXT;
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
