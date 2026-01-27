package gov.cms.smart.pages;


import gov.cms.smart.utils.ConfigReader;
import gov.cms.smart.utils.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;
    private UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();

    By username = By.id("username");
    By password = By.id("password");
    By loginButton = By.id("Login");

    public LoginPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void loginAsOSGUser() {
        logger.info("Signing in to Salesforce as a OSG user...");
        if (utils.getSalesforceEnv().contains("qa")) {
            utils.sendKeys(username, ConfigReader.getUserName("osg", "qa"));
            utils.sendKeys(password, ConfigReader.getPassword());
            utils.clickElement(loginButton);
        } else {
            utils.sendKeys(username, ConfigReader.getUserName("osg", "dev"));
            utils.sendKeys(password, ConfigReader.getPassword());
            utils.clickElement(loginButton);
        }
        logger.info("Signing in to Salesforce as OSG user was successful.");
    }

}

