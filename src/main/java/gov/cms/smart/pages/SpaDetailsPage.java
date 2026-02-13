package gov.cms.smart.pages;

import gov.cms.smart.models.*;
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

    public PlanInfo readPlanInfo() {
        PlanInfo planInfo = new PlanInfo();
        planInfo.setSubject(utils.getFieldTextByLabel("Subject"));
        planInfo.setDescription(utils.getFieldTextByLabel("Description"));
        return planInfo;
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
