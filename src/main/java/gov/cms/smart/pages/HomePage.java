package gov.cms.smart.pages;

import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By WAIVERS_SPA = By.xpath("//span[text()=\"SPAs or Waivers\"]/parent::a");
    private static final Logger logger = LogManager.getLogger();

    public HomePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public SPAsWaiversPage goToSpasWaiversPage() {
        utils.javaScriptClicker(WAIVERS_SPA);
        logger.info("Navigated to SPAs & Waivers.");
        return PageFactory.getSpaWaiversPage(driver, utils);
    }

}
