package gov.cms.smart.pages;


import gov.cms.smart.utils.ConfigReader;
import gov.cms.smart.utils.PageFactory;
import gov.cms.smart.utils.UIElementUtils;
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

    public HomePage loginAsOSGUser() throws Exception {
        //String host = "imap.gmail.com";
        logger.info("Signing in to Salesforce as a OSG user...");
        if (utils.getEnv().contains("qa")) {
            utils.sendKeys(USERNAME, ConfigReader.getUserName("osg", "qa"));
            utils.sendKeys(PASSWORD, ConfigReader.getPassword());
            utils.clickElement(LOGIN_BUTTON);

            // Use the secret you copied in Step 1 (remove spaces)
            String sharedSecret = "";
            Totp totp = new Totp(sharedSecret);
            String mfaCode = totp.now(); // This generates the code Salesforce expects RIGHT NOW
            System.out.println(mfaCode);
// Now use Selenium to type mfaCode into the Salesforce MFA field
            //   driver.findElement(By.id("totp_field_id")).sendKeys(mfaCode);
            //  String otp = EmailOtpFetcher.fetchOtp(host, ConfigReader.get("email"), ConfigReader.get("APP_PASSWORD"), 20, 1);
            utils.sendKeys(VERIFICATION_INPUT, mfaCode);
            utils.clickElement(VERIFY);
        } else {
            utils.sendKeys(USERNAME, ConfigReader.getUserName("osg", "dev"));
            utils.sendKeys(PASSWORD, ConfigReader.getPassword());
            utils.clickElement(LOGIN_BUTTON);
        }
        logger.info("Signing in to Salesforce as OSG user was successful.");
        return PageFactory.getHomePage(driver, utils);
    }

}

