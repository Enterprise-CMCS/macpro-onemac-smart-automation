package gov.cms.smart.flows;

import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.pages.HomePage;
import gov.cms.smart.utils.config.TestContext;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class CPOCUser {

    private static final Logger logger = LogManager.getLogger();
    private final WebDriver driver;
    private final UIElementUtils utils;


    public CPOCUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public HomePage loginWithSharedSecret(){
        String cpocSharedSecret = TestContext.cpocSharedSecret();
        String cpocUsername = TestContext.cpocUsername();
        return PageFactory.getLoginPage(driver, utils).loginWithSharedSecret(cpocUsername,cpocSharedSecret);
    }

    public HomePage login(){
        return PageFactory.getLoginPage(driver, utils).login(TestContext.cpocUsername());
    }

 /*   public LoginPage navigateToSalesForce() {
        logger.info("Navigating To Salesforce...");
        driver.get(utils.getEnv());
        return PageFactory.getLoginPage(driver, utils);
    }*/

    public void goToNewMedicaidSPAForm() {
        PageFactory.getSpaWaiversPage(driver, utils).clickNew();
    }

    public void goToSPAWaiversPage() {
        PageFactory.getHomePage(driver, utils).goToSpasWaiversPage();
    }
    public void openRecordFromAllRecordsView(SpaPackage spaPackage) throws InterruptedException {
        PageFactory.getHomePage(driver, utils).goToSpasWaiversPage().openRecordFromAllRecordsView(spaPackage);
    }

    public void createSPA(String state) throws InterruptedException {
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
