package gov.cms.smart.utils.ui;

import gov.cms.smart.utils.config.ConfigReader;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    private final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("M/d/yyyy");

    public String calculateDaysFromToday(String givenDate) {

        LocalDate inputDate = LocalDate.parse(givenDate, FORMATTER);
        LocalDate today = LocalDate.now();

        long days = ChronoUnit.DAYS.between(inputDate, today);

        return String.valueOf(days);
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

    public void scrollToElement(By locator) {
        WebElement element = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }


    public void scrollIntoView(WebElement element) {
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


    public void waitUntilTopNavAnchorReady(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(d -> {
            try {
                WebElement el = d.findElement(locator);

                if (!el.isDisplayed() || !el.isEnabled()) {
                    return false;
                }

                JavascriptExecutor js = (JavascriptExecutor) d;

                return Boolean.TRUE.equals(js.executeScript(
                        "const el = arguments[0];" +
                                "const r = el.getBoundingClientRect();" +
                                "if (r.width === 0 || r.height === 0) return false;" +
                                "const x = r.left + r.width / 2;" +
                                "const y = r.top + r.height / 2;" +
                                "const topEl = document.elementFromPoint(x, y);" +
                                "return topEl === el || el.contains(topEl);",
                        el
                ));
            } catch (Exception e) {
                return false;
            }
        });
    }
    public void clickElement(By locator) {
        WebElement element = waitForClickable(locator);
        scrollIntoView(element);

        if ("lightning-base-combobox".equalsIgnoreCase(element.getTagName())) {
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
            element = waitForClickable(locator); // re-find
            scrollIntoView(element);
            new Actions(driver).moveToElement(element).pause(Duration.ofMillis(200)).click().perform();
            return;
        } catch (Exception e) {
            logger.warn("Actions click failed, using JS click");
        }

        element = waitForClickable(locator); // re-find again
        scrollIntoView(element);
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

    public List<String> getValuesFromDropdownByLabel(String label) {
        List<String> list = new ArrayList<>();
        clickElement(By.xpath("//label[text()=\"" + label + "\"]//../parent::div/child::div/lightning-base-combobox"));
        By optionLocator = By.xpath("//label[text()=\"" + label + "\"]//../parent::div/child::div/lightning-base-combobox//lightning-base-combobox-item/span/span");
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(optionLocator));
        for (WebElement option : options) {
            String text = cleanString(option.getText());
            list.add(text.trim());
        }
        return list;

    }

    public void clearTextAreaByLabel(String label) {
        By locator = By.xpath("//label[text()=\"" + label + "\"]/following-sibling::div/textarea");
        waitForVisible(locator).clear();
    }

    public String getFieldTextByLabel(String label) {
        By locator = By.xpath("//span[text()=\"" + label + "\"]/parent::div/following-sibling::div//lightning-formatted-text");
        return getText(locator);
    }

    public boolean waitForTextToBePresent(By locator, String expectedText) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
    }

    public void waitForFieldTextToBe(String sectionName, String fieldName, String fieldTextValue) {
        By field = By.xpath("//span[text()=\"" + sectionName + "\"]/ancestor::flexipage-field-section2//span[text()=\"" + fieldName + "\"]/../following-sibling::div//lightning-formatted-text");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(field, fieldTextValue));
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

    public List<WebElement> waitForRecord(By locator) {
        return wait.until(ExpectedConditions.numberOfElementsToBe(locator, 1));
    }

    public void waitForNumberOfElementsToBe(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /* -------------------- DATE UTILITIES (UNCHANGED LOGIC) -------------------- */

    public String calculateDate(String initialSubmissionDate, int days) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(initialSubmissionDate, dateTimeFormatter);
        LocalDate newDate = date.plusDays(days);
        return newDate.format(dateTimeFormatter);
    }

    public String getTodayDateFormatted() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public String calculateDaysRemaining(String daysOnActiveClock) {

        int activeDays = Integer.parseInt(daysOnActiveClock.trim());

        int daysRemaining = 90 - activeDays;

        return String.valueOf(daysRemaining);
    }

    public static String getFutureDateByDays(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    public String extractDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.format(DateTimeFormatter.ofPattern("dd"));
    }

    public String getPastDate(int days) {
        LocalDate date = LocalDate.now().minusDays(days);
        return date.format(DateTimeFormatter.ofPattern("M/d/yyyy"));
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

    // Helper method to clean a string
    public static String cleanString(String str) {
        if (str == null) return "";
        return str.trim()
                .replace('\u00A0', ' ') // non-breaking space → normal space
                .replaceAll("[\\p{Cf}\\p{Zs}]+", " ")  // Remove all format chars and normalize spaces
                .replaceAll("\\s+", " ")  // normalize multiple spaces
                .trim();
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

    public void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));
    }

    public void waitForSalesforceLoading(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

        // 1. Browser load
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        // 2. Lightning overlay spinner
        wait.ignoring(StaleElementReferenceException.class)
                .until(driver1 -> {
                    List<WebElement> overlays =
                            driver1.findElements(By.cssSelector(".slds-spinner_container"));

                    for (WebElement overlay : overlays) {
                        if (overlay.isDisplayed()) {
                            return false; // still loading
                        }
                    }
                    return true;
                });

        // 3. Standard spinner (recommended to include)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".slds-spinner")));
    }


    public void waitForSalesforcePageToFullyLoad(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // 1. Wait for document ready
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        // 2. Wait for Lightning spinners
        wait.until(driver1 -> {
            List<WebElement> spinners = driver1.findElements(By.cssSelector(".slds-spinner"));
            return spinners.isEmpty() || spinners.stream().allMatch(e -> !e.isDisplayed());
        });

        // 3. Wait for Aura overlay
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".auraLoadingBox")));
    }

    public void waitForLightningApp(WebDriver driver) {
        waitForPageLoad(driver);
        waitForSalesforcePageToFullyLoad(driver);
    }


    public void waitForSalesforceSpinnerToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".slds-spinner")));
        } catch (TimeoutException e) {
            System.out.println("Spinner not found or already disappeared.");
        }
    }

    public void openRecord(String recordId) {
        //   waitForNumberOfElementsToBe(By.xpath("//lightning-datatable//tbody/tr"), 1);
        clickElement(By.xpath("//lightning-datatable//tbody/tr/td[2]/ancestor::tr/th//a[@title=\"" + recordId + "\"]"));
        // driver.findElement(By.xpath("//lightning-datatable//tbody/tr/td[2]/ancestor::tr/th//a[@title=\"" + recordId + "\"]")).click();
    }
}
