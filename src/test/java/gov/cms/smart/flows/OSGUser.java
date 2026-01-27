package gov.cms.smart.flows;
import gov.cms.smart.utils.PageFactory;
import gov.cms.smart.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;

public class OSGUser {

    private static final Logger logger = LogManager.getLogger();
    private WebDriver driver;
    private UIElementUtils utils;


    public OSGUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void login() {
        PageFactory.getLoginPage(driver, utils).loginAsOSGUser();
    }

    public void navigateToSalesForce() {
        logger.info("Navigating To Salesforce...");
        driver.get(utils.getSalesforceEnv());
    }

    public void goToSPAWaiversPage() {
        PageFactory.getHomePage(driver, utils).goToSpasWaiversPage();
    }

    public void enterSPADetails() {
        PageFactory.getSPAPage(driver, utils).enterSPADetails("AL");
    }

    public void modelTest() throws InterruptedException {
        PageFactory.getSPAPage(driver, utils).modelTest();
    }

    public void navigateToMedicaidSPAForm() {
        PageFactory.getSPAPage(driver, utils).navigateToMedicaidSPAForm();
    }


    public void enterInitialSubmissionDate(String date) {
        PageFactory.getSPAPage(driver, utils).enterInitialSubmissionDate(date);
        PageFactory.getSPAPage(driver, utils).clickSave();
    }

    public boolean isIDNumberErrorDisplayed() {
        PageFactory.getSPAPage(driver, utils).clickSave();
        return PageFactory.getSPAPage(driver, utils).isIDNumberErrorDisplayed();
    }

    public boolean isInitialSubmissionDateErrorDisplayed() {
        PageFactory.getSPAPage(driver, utils).clickSave();
        return PageFactory.getSPAPage(driver, utils).isInitialSubmissionDateErrorDisplayed();
    }

    public boolean isStateErrorDisplayed() {
        return PageFactory.getSPAPage(driver, utils).isStateErrorDisplayed();
    }

    public boolean isDateFormatErrorDisplayed() throws InterruptedException {
        return PageFactory.getSPAPage(driver, utils).isDateFormatErrorDisplayed();
    }
}
