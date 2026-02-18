package gov.cms.smart.utils.ui;

import gov.cms.smart.utils.config.ConfigReader;
import org.apache.groovy.json.internal.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIElementUtils {

    private static final Logger logger = LogManager.getLogger();

    private WebDriver driver;
    private WebDriverWait wait;

    public UIElementUtils(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    /* -------------------- WAIT HELPERS -------------------- */
    public boolean isElementVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementInvisible(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /* -------------------- CORE INTERACTIONS -------------------- */
    private void waitForVisibleAndSize(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> element.isDisplayed() &&
                element.getSize().getHeight() > 0 &&
                element.getSize().getWidth() > 0);
    }

    public void clickElement(By locator) {
        WebElement element = waitForClickable(locator);
        scrollIntoView(element);

        // If this is a lightning combobox wrapper, find the internal button
        if (element.getTagName().equals("lightning-base-combobox")) {
            try {
                WebElement button = element.findElement(By.xpath(".//button"));
                waitForVisibleAndSize(button);
                jsClick(button);
                return;
            } catch (Exception e) {
                logger.warn("Lightning combobox button click failed, fallback to original element");
            }
        }

        try {
            element.click();
            return;
        } catch (Exception e) {
            logger.warn("Standard click failed, trying Actions click");
        }

        try {
            new Actions(driver).moveToElement(element).pause(Duration.ofMillis(200)).click().perform();
            return;
        } catch (Exception e) {
            logger.warn("Actions click failed, using JS click");
        }

        jsClick(element);
    }


    public void sendKeys(By locator, String text) {
        WebElement element = waitForVisible(locator);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeys(By locator, Keys keys) {
        waitForVisible(locator).sendKeys(keys);
    }

    public void clearInput(By locator) {
        WebElement element = waitForVisible(locator);
        element.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
    }

    public boolean isVisible(By locator) {
        try {
            waitForVisible(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getEnv() {
        if (ConfigReader.get("runOn").equalsIgnoreCase("qa")) {
            return ConfigReader.get("QA");
        } else {
            return ConfigReader.get("DEV");
        }
    }

    public boolean isClickable(By locator) {
        try {
            waitForClickable(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            return waitForVisible(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNotVisible(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    public void javaScriptClicker(By locator) {
        jsClick(waitForPresence(locator));
    }

    /* -------------------- LABEL-BASED METHODS -------------------- */

    public String sendKeysToInputByLabel(String label, String text) {
        By locator = By.xpath("//label[text()=\"" + label + "\"]/following-sibling::div/input");
        sendKeys(locator, text);
        return text;
    }

    public void clearInputByLabel(String label) {
        By locator = By.xpath("//label[text()=\"" + label + "\"]/following-sibling::div/input");
        clearInput(locator);
    }

    public String sendKeysToTextAreaByLabel(String label, String text) {
        By locator = By.xpath("//label[text()=\"" + label + "\"]/following-sibling::div/textarea");
        sendKeys(locator, text);
        return text;
    }

    public void selectFromComboBoxByLabel(String label, String value) throws InterruptedException {
        clickElement(By.xpath("//label[text()=\"" + label + "\"]//../parent::div/child::div/lightning-base-combobox"));
        selectDropdownBy(By.xpath("//label[text()=\"" + label + "\"]//../parent::div/child::div/lightning-base-combobox//lightning-base-combobox-item/span/span"), value);
    }

    public void clearTextAreaByLabel(String label) {
        By locator = By.xpath("//label[text()=\"" + label + "\"]/following-sibling::div/textarea");
        waitForVisible(locator).clear();
    }

    public String getFieldTextByLabel(String label) {
        By locator = By.xpath("//span[contains(text(),\"" + label + "\")]/parent::div/following-sibling::div//lightning-formatted-text");
        return getText(locator);
    }

    /* -------------------- DROPDOWNS -------------------- */

    public void selectDropdownBy(By optionLocator, String value) throws InterruptedException {
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(optionLocator));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(value)) {
                WebElement parent = option.findElement(By.xpath("./.."));
                scrollIntoView(parent);
                parent.click();
                return;
            }
        }

        throw new NoSuchElementException("Dropdown option not found: " + value);
    }

    /* -------------------- FILE UPLOAD -------------------- */

    public void uploadFile(String filePath, By locator) {
        File file = new File(filePath);
        waitForPresence(locator).sendKeys(file.getAbsolutePath());
    }

    /* -------------------- WAIT UTILITIES -------------------- */

    public void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForText(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public void waitForNumberOfElementsToBe(By locator, int num) {
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, num));
    }

    public void waitForNumberOfElementsToBe(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    /* -------------------- DATE UTILITIES (UNCHANGED LOGIC) -------------------- */

    public String getTodayDateFormatted() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public static String getFutureDateByDays(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    public String extractDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.format(DateTimeFormatter.ofPattern("dd"));
    }

    /* -------------------- STATE HELPERS -------------------- */

    public String getStateCode(String input) {
        if (input == null || !input.contains("-")) return null;
        return input.substring(0, input.indexOf('-'));
    }

    public String removeStateCode(String input) {
        if (input == null || !input.contains("-")) return input;
        return input.substring(input.indexOf('-') + 1);
    }


    public String getStateFullName(String abbreviation) {

        if (abbreviation == null || abbreviation.trim().length() != 2) {
            return "Unknown State";
        }
        String key = abbreviation.trim().toUpperCase();

        Map<String, String> states = new HashMap<>();
        states.put("AL", "Alabama");
        states.put("AK", "Alaska");
        states.put("AZ", "Arizona");
        states.put("AR", "Arkansas");
        states.put("CA", "California");
        states.put("CO", "Colorado");
        states.put("CT", "Connecticut");
        states.put("DE", "Delaware");
        states.put("FL", "Florida");
        states.put("GA", "Georgia");
        states.put("HI", "Hawaii");
        states.put("ID", "Idaho");
        states.put("IL", "Illinois");
        states.put("IN", "Indiana");
        states.put("IA", "Iowa");
        states.put("KS", "Kansas");
        states.put("KY", "Kentucky");
        states.put("LA", "Louisiana");
        states.put("ME", "Maine");
        states.put("MD", "Maryland");
        states.put("MA", "Massachusetts");
        states.put("MI", "Michigan");
        states.put("MN", "Minnesota");
        states.put("MS", "Mississippi");
        states.put("MO", "Missouri");
        states.put("MT", "Montana");
        states.put("NE", "Nebraska");
        states.put("NV", "Nevada");
        states.put("NH", "New Hampshire");
        states.put("NJ", "New Jersey");
        states.put("NM", "New Mexico");
        states.put("NY", "New York");
        states.put("NC", "North Carolina");
        states.put("ND", "North Dakota");
        states.put("OH", "Ohio");
        states.put("OK", "Oklahoma");
        states.put("OR", "Oregon");
        states.put("PA", "Pennsylvania");
        states.put("RI", "Rhode Island");
        states.put("SC", "South Carolina");
        states.put("SD", "South Dakota");
        states.put("TN", "Tennessee");
        states.put("TX", "Texas");
        states.put("UT", "Utah");
        states.put("VT", "Vermont");
        states.put("VA", "Virginia");
        states.put("WA", "Washington");
        states.put("WV", "West Virginia");
        states.put("WI", "Wisconsin");
        states.put("WY", "Wyoming");
        return states.getOrDefault(key, "Unknown State");
    }

    public void editByLabel(String label) {
        clickElement(By.xpath("//span[text()=\"" + label + "\"]/../following-sibling::div//button"));
        isVisible(By.xpath("//button[text()=\"Save\"]"));
    }

    public boolean elementContainsText(WebDriver driver, By locator, String expectedText) {
        try {
            return driver.findElement(locator)
                    .getText()
                    .contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }

    public void openRecord(String recordId) {
        waitForNumberOfElementsToBe(By.xpath("//lightning-datatable//tbody/tr"), 1);
        driver.findElement(By.xpath("//lightning-datatable//tbody/tr/td[2]/ancestor::tr/th//a[@title=\"" + recordId + "\"]")).click();
    }
}
