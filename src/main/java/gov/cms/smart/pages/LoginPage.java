package gov.cms.smart.pages;


import gov.cms.smart.utils.ConfigReader;
import gov.cms.smart.utils.PageFactory;
import gov.cms.smart.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();
    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("Login");

    public LoginPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public HomePage loginAsOSGUser() {
        logger.info("Signing in to Salesforce as a OSG user...");
        if (utils.getEnv().contains("qa")) {
            utils.sendKeys(USERNAME, ConfigReader.getUserName("osg", "qa"));
            utils.sendKeys(PASSWORD, ConfigReader.getPassword());
            utils.clickElement(LOGIN_BUTTON);
        } else {
            utils.sendKeys(USERNAME, ConfigReader.getUserName("osg", "dev"));
            utils.sendKeys(PASSWORD, ConfigReader.getPassword());
            utils.clickElement(LOGIN_BUTTON);
        }
        logger.info("Signing in to Salesforce as OSG user was successful.");
        return PageFactory.getHomePage(driver, utils);
    }

}

