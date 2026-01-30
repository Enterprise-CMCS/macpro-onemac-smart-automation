package gov.cms.smart.pages;

import gov.cms.smart.models.IdentifyingInfo;
import gov.cms.smart.models.PackageDetails;
import gov.cms.smart.models.PriorityInfo;
import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import gov.cms.smart.utils.PageFactory;
import gov.cms.smart.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SpaDetailsPage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By SAVE = By.xpath("//button[text()=\"Save\"]");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div[data-key=\"success\"]");
    // ===== IDENTIFYING INFO =====
    //private By idNumber = By.id("spaId");
    //  private By state = By.id("state");


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

    public PriorityInfo fillPriorityInfo(PriorityCode priorityCode, CodingAssessment codingAssessment) {
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
        Duration originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(SAVE));
        driver.manage().timeouts().implicitlyWait(originalImplicitWait);
        //  utils.isVisible(SUCCESS_MESSAGE);
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

    public SpaDetailsPage test(SpaPackage spaPackage, String priorityCode) {
        PackageDetails details = new PackageDetails();

        //  utils.selectFromComboBox("Priority Code", priorityCode);
        //  utils.clickElement(By.xpath("//span[text()=\"Coding After Initial Assessment\"]/../following-sibling::div//button"));
        // driver.findElement(By.xpath("//span[text()=\"Coding After Initial Assessment\"]/../following-sibling::div//button")).click();
        //utils.isVisible(By.xpath("//button[text()=\"Save\"]"));
        // utils.clickElement(By.xpath("//label[text()=\"Coding After Initial Assessment\"]//../parent::div/child::div/lightning-base-combobox/div"));
        //utils.selectDropdownBy(COMBO_BOX_ITEMS, "Same");
        // utils.selectFromComboBox("Coding After Initial Assessment","Up Coded");

        // Identifying Info
       /* IdentifyingInfo identifyingInfo = new IdentifyingInfo();
        identifyingInfo.setIdNumber(spaPackage.getPackageId());
        identifyingInfo.setState(spaPackage.getState());
        identifyingInfo.setAuthority(spaPackage.getAuthority());*/

        // Priority
      /* PriorityInfo priority = new PriorityInfo();
       priority.setPriorityCode("Routine Review");*/
        // Service & Status

        //  PriorityInfo priority = new PriorityInfo();

        //  priority.setPriorityCode(TestDataFactory.randomPriorityCode());
/*
       ServiceStatusInfo service = new ServiceStatusInfo();
       service.setEffectiveDate("01/01/2026");
       service.setServiceType("Home & Community Based");
*/

// Add to master details
        //  details.setIdentifyingInfo(identifyingInfo);
        //System.out.println(details.getIdentifyingInfo().getAuthority());

        //  details.setPriorityInfo(priority);
        // details.setServiceStatusInfo(service);
        return PageFactory.getSpaDetailsPage(driver, utils);
    }

   /* public PriorityInfo set() {
        IdentifyingInfo info = new IdentifyingInfo();
        info.setIdNumber(utils.getFieldTextByLabel("ID Number"));
        info.setAuthority(utils.getFieldTextByLabel("Authority"));
        info.setState(utils.getFieldTextByLabel("State"));
        return info;
    }*/


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
