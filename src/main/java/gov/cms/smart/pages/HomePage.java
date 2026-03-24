package gov.cms.smart.pages;

import gov.cms.smart.utils.driver.PageFactory;
import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final By WAIVERS_SPA = By.xpath("//span[text()=\"SPAs or Waivers\"]/ancestor::a");
    private static final By ALERT = By.xpath("//lightning-formatted-rich-text/span/p/strong[text()=\"ALERT!\"]");
    private static final Logger logger = LogManager.getLogger();
    private static final By SEARCH_INPUT = By.xpath("//lst-list-view-manager-settings-menu/parent::div/..//force-list-view-manager-search-bar//input");

    public HomePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public boolean isSPATabDisplayed() {
        return utils.isVisible(WAIVERS_SPA);
    }

    public SPAsWaiversPage goToSpasWaiversPage() {
        logger.info("Navigating to SPAs & Waivers page...");

        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int attempts = 0;
        int maxAttempts = 5;

        while (attempts < maxAttempts) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(WAIVERS_SPA));
                actions.moveToElement(element).click().perform();
                // Wait briefly to see if the search input appears
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.presenceOfElementLocated(SEARCH_INPUT));
                logger.info("Successfully navigated to SPAs & Waivers page.");
                return PageFactory.getSpaWaiversPage(driver, utils);

            } catch (TimeoutException e) {
                attempts++;
                logger.warn("SPAs & Waivers page not loaded yet. Retrying click... Attempt {}", attempts);

                try {
                    Thread.sleep(500); // small stabilization pause
                } catch (InterruptedException ignored) {
                }
            }
        }

        throw new RuntimeException("Failed to navigate to SPAs & Waivers page after " + maxAttempts + " attempts.");
    }
}


