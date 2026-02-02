package gov.cms.smart.flows;
import gov.cms.smart.pages.HomePage;
import gov.cms.smart.pages.LoginPage;
import gov.cms.smart.utils.ExcelPackageSelector;
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

    public HomePage login() {
        return PageFactory.getLoginPage(driver, utils).loginAsOSGUser();
    }

    public LoginPage navigateToSalesForce() {
        logger.info("Navigating To Salesforce...");
        driver.get(utils.getEnv());
        logger.info("Selected SPA: "+ExcelPackageSelector.selectSpa("AL","Medicaid SPA",""));
        //
       return PageFactory.getLoginPage(driver,utils);
    }
    public void goToNewMedicaidSPAForm(){
        PageFactory.getSpaWaiversPage(driver,utils).clickNew();
    }

    public void goToSPAWaiversPage() {
        PageFactory.getHomePage(driver, utils).goToSpasWaiversPage();
    }

    public void createSPA(String state) {
        PageFactory.getSPAPage(driver, utils).createSPA(state);
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

    /*public boolean isDateFormatErrorDisplayed() throws InterruptedException {
        return PageFactory.getSPAPage(driver, utils).isDateFormatErrorDisplayed();
    }*/

}
