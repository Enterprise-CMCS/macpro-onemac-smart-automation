package gov.cms.smart.pages;


import gov.cms.smart.utils.config.TestContext;
import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();
    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("Login");
    private static final By VERIFICATION_INPUT = By.cssSelector("input[name=\"tc\"]");
    private static final By VERIFY = By.cssSelector("input[title=\"Verify\"]");

    public LoginPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public HomePage loginWithSharedSecret(String username, String sharedSecret) {
        logger.info("Signing in to Salesforce as: {}", username);
        utils.sendKeys(USERNAME, username);
        utils.sendKeys(PASSWORD, TestContext.password());
        utils.clickElement(LOGIN_BUTTON);
        // Wait for MFA input to appear
        utils.waitForVisibility(VERIFICATION_INPUT); // 10 sec timeout
        // Generate TOTP code **after MFA input is ready**
        Totp totp = new Totp(sharedSecret);
        String mfaCode = totp.now();
        utils.sendKeys(VERIFICATION_INPUT, mfaCode);
        utils.clickElement(VERIFY);
        logger.info("Signing in to Salesforce as: {} user was successful.", username);
        return PageFactory.getHomePage(driver, utils);
    }

    public HomePage login(String username) {
        logger.info("Signing in to Salesforce as: {}", username);
        utils.sendKeys(USERNAME, username);
        utils.sendKeys(PASSWORD, TestContext.password());
        utils.clickElement(LOGIN_BUTTON);
        logger.info("Signing in to Salesforce as: {} user was successful.", username);
        return PageFactory.getHomePage(driver, utils);
    }

}



