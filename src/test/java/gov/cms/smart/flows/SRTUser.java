package gov.cms.smart.flows;

import gov.cms.smart.pages.HomePage;
import gov.cms.smart.utils.config.TestContext;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class SRTUser {

    private static final Logger logger = LogManager.getLogger();
    private final WebDriver driver;
    private final UIElementUtils utils;


    public SRTUser(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public HomePage loginWithSharedSecret(){
        String srtSharedSecret = TestContext.srtSharedSecret();
        String srtUsername = TestContext.srtUsername();
        return PageFactory.getLoginPage(driver, utils).loginWithSharedSecret(srtUsername,srtSharedSecret);
    }
    public HomePage login(){
        String srtUsername = TestContext.srtUsername();
        return PageFactory.getLoginPage(driver, utils).login(srtUsername);
    }

    public HomePage loginWithSRT2SharedSecret(){
        String srtSharedSecret = TestContext.srt2SharedSecret();
        String srtUsername = TestContext.srtUsername();
        return PageFactory.getLoginPage(driver, utils).loginWithSharedSecret(srtUsername,srtSharedSecret);
    }

    public HomePage loginAsSRT2(){
        return PageFactory.getLoginPage(driver, utils).login(TestContext.srt2Username());
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

    public void createSPA(String state) throws InterruptedException {
        PageFactory.getNewSPAPage(driver, utils).createSPA(state);
    }


    public void navigateToMedicaidSPAForm() {
        PageFactory.getNewSPAPage(driver, utils).navigateToMedicaidSPAForm();
    }


    public void enterInitialSubmissionDate(String date) {
        PageFactory.getNewSPAPage(driver, utils).enterInitialSubmissionDate(date);
        PageFactory.getNewSPAPage(driver, utils).clickSave();
    }

    public boolean isIDNumberErrorDisplayed() {
        PageFactory.getNewSPAPage(driver, utils).clickSave();
        return PageFactory.getNewSPAPage(driver, utils).isIDNumberErrorDisplayed();
    }

    public boolean isInitialSubmissionDateErrorDisplayed() {
        PageFactory.getNewSPAPage(driver, utils).clickSave();
        return PageFactory.getNewSPAPage(driver, utils).isInitialSubmissionDateErrorDisplayed();
    }

    public boolean isStateErrorDisplayed() {
        return PageFactory.getNewSPAPage(driver, utils).isStateErrorDisplayed();
    }

    /*public boolean isDateFormatErrorDisplayed() throws InterruptedException {
        return PageFactory.getSPAPage(driver, utils).isDateFormatErrorDisplayed();
    }*/

}
