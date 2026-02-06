package gov.cms.smart.pages;

import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By WAIVERS_SPA = By.xpath("//span[text()=\"SPAs or Waivers\"]/parent::a");

    public HomePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public SPAsWaiversPage goToSpasWaiversPage() {
        utils.javaScriptClicker(WAIVERS_SPA);
        return PageFactory.getSpaWaiversPage(driver, utils);
    }

}
