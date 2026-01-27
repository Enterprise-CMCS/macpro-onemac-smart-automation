package gov.cms.smart.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UIElementUtils {

    private static final Logger logger = LogManager.getLogger();

    private String proposedEffectiveDate = getPastBusinessDate(14);
    private String initialSubmissionDate = getPastBusinessDate(14);
    private String raiRequestDate = getPastBusinessDate(12);
    private String raiResponseDate = getPastBusinessDate(10);
    private String raiResponseWithdrawnDate = getPastBusinessDate(7);
    private String secondRaiRequestDate = getPastBusinessDate(5);

    private WebDriver driver;

    public String getProposedEffectiveDate() {
        return proposedEffectiveDate;
    }

    public String getInitialSubmissionDate() {
        return initialSubmissionDate;
    }

    public String getRaiRequestDate() {
        return raiRequestDate;
    }

    public String getSecondRaiRequestDate() {
        return secondRaiRequestDate;
    }

    public String getRaiResponseDate() {
        return raiResponseDate;
    }

    public String getRaiResponseWithdrawnDate() {
        return raiResponseWithdrawnDate;
    }

    private WebDriverWait wait;

    public String getFieldTextByLabel(String label) {
        return driver.
                findElement(By.
                        xpath("//span[contains(text(),\"" + label + "\")]/parent::div/following-sibling::div//lightning-formatted-text")).
                getText();
    }

    public String getSalesforceEnv() {
        if (ConfigReader.get("runOn").equalsIgnoreCase("qa")) {
            return ConfigReader.get("QA");
        } else {
            return ConfigReader.get("DEV");
        }
    }

    public void selectDropdownBy(By optionLocator, String value) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Wait until dropdown options are visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
        List<WebElement> options = driver.findElements(optionLocator);
        for (WebElement option : options) {
            String text = option.getText().trim();
            if (text.equalsIgnoreCase(value)) {
                // Parent is the clickable lightning-base-combobox-item
                WebElement parent = option.findElement(By.xpath("./.."));
                // Scroll into view
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", parent);
                // Standard Selenium click
                try {
                    parent.click();
                    return;
                } catch (Exception e1) {
                    //JavaScript click fallback
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", parent);
                        return;

                    } catch (Exception e2) {
                        // Actions click fallback
                        try {
                            new Actions(driver)
                                    .moveToElement(parent)
                                    .click()
                                    .perform();
                            return;
                        } catch (Exception e3) {
                            throw new RuntimeException(
                                    "Failed to click dropdown option: " + value, e3);
                        }
                    }
                }
            }
        }

        throw new NoSuchElementException("Dropdown option not found: " + value);
    }

    public UIElementUtils(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public void selectFromComboBox(String label, String value) {
        clickElement(By.xpath("//span[text()=\"" + label + "\"]/../following-sibling::div//button"));
        isVisible(By.xpath("//button[text()=\"Save\"]"));
        clickElement(By.xpath("//label[text()=\"" + label + "\"]//../parent::div/child::div/lightning-base-combobox"));
        selectDropdownBy(By.xpath("//lightning-base-combobox-item/span/span"), value);
    }

    public boolean isNotVisible(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            if (elements.isEmpty()) {
                return true;
            }
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void clickElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        // Scroll into view (important for headless)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);

        try {
            element.click();
            logger.info("Normal click performed successfully.");
            return;
        } catch (ElementClickInterceptedException e) {
            logger.info("Normal click intercepted: {}", e.getMessage());
        } catch (Exception e) {
            logger.info("Normal click failed: {}", e.getMessage());
        }

        // Actions click fallback
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
            logger.info("Actions click performed successfully.");
            return;
        } catch (Exception actionsEx) {
            logger.info("Actions click failed: {}", actionsEx.getMessage());
        }

        // JS click fallback
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("JavaScript click performed successfully.");
        } catch (Exception jsEx) {
            logger.info("JavaScript click failed: {}", jsEx.getMessage());
        }
    }


    public void saveSpa(By locator) {
        clickElement(locator);
    }

    public void uploadFile(String filePath, By locator) {
        File file = new File(filePath);
        sendKeys(locator, file.getAbsolutePath());

    }

    public void sleepFor(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public boolean isVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForElementToBeStableAndEnabled(By locator, int stableMillis, int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000;

        while (System.currentTimeMillis() < endTime) {
            try {
                WebElement el = driver.findElement(locator);

                // must be enabled first
                if (!el.isEnabled())
                    continue;

                // now verify stable state
                long stabilityStart = System.currentTimeMillis();
                boolean stable = true;

                while (System.currentTimeMillis() - stabilityStart < stableMillis) {
                    WebElement check = driver.findElement(locator);
                    if (!check.isEnabled()) {
                        stable = false;
                        break; // button flickered, restart
                    }
                    Thread.sleep(50);
                }

                if (stable)
                    return true; // element is truly ready

            } catch (StaleElementReferenceException ignored) {
                // React re-rendering — retry
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        return false; // element never stabilized
    }

    public void waitForThreeDotsLoadingToDisappear() {
        By loader = By.cssSelector("div[data-testid='three-dots-loading']");
        Duration originalWait = driver.manage().timeouts().getImplicitWaitTimeout();

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.pollingEvery(Duration.ofMillis(300));

            wait.until(d -> {
                List<WebElement> loaders = d.findElements(loader);

                // not present → done
                if (loaders.isEmpty()) return true;

                // present but not visible → done
                return loaders.stream().noneMatch(WebElement::isDisplayed);
            });

        } catch (Exception ignored) {
            // Optional: log timeout but do not fail test
        } finally {
            // Restore global implicit wait (15s)
            driver.manage().timeouts().implicitlyWait(originalWait);
        }
    }


    public void waitForNumberOfElementsToBe(By locator, int num) {
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, num));
    }

    public void waitForSingleRecordAndClick() {
        By pageLocation = By.cssSelector("[data-testid='page-location']");
        By firstRowLink = By.xpath("//tbody/tr[1]/td[2]/a");
        wait.until(d -> {
            try {
                String raw = driver.findElement(pageLocation).getText();
                // Normalize: replace newline with space, collapse multiple spaces
                String normalized = raw.replace("\n", " ").replaceAll("\\s+", " ").trim();
                return normalized.equals("1 - 1 of 1 records");
            } catch (Exception e) {
                return false;
            }
        });
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(firstRowLink));
        link.click();
    }


    public String extractDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.format(DateTimeFormatter.ofPattern("dd"));
    }

    public void clickMyAccount(By myAccount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(myAccount));

        // Scroll element into view
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);

        // Try normal click first
        try {
            logger.info("Attempting normal click on My Account...");
            element.click();
            return;
        } catch (Exception e) {
            logger.warn("Normal click failed: {}", e.getMessage());
        }

        // Try Actions click
        try {
            logger.info("Trying Actions click for My Account...");
            new Actions(driver)
                    .moveToElement(element)
                    .pause(Duration.ofMillis(100))
                    .click()
                    .perform();
            return;
        } catch (Exception e) {
            logger.warn("Actions click failed: {}", e.getMessage());
        }

        // Final fallback — safe JS click
        try {
            logger.info("Falling back to JavaScript click for My Account...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            logger.error("JavaScript click failed as well: {}", e.getMessage());
            throw e;     // Rethrow after all fallbacks fail
        }
    }


    public String getTodayDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.now().format(formatter);
    }

    public boolean refreshUntilInvisible(By locator, int timeoutSeconds) {
        // Store original implicit wait
        Duration originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();

        try {
            // Temporarily reduce implicit wait for faster polling
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

            long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000);

            while (System.currentTimeMillis() < endTime) {
                try {
                    WebElement element = driver.findElement(locator);

                    // If found AND visible keep waiting
                    if (!element.isDisplayed()) {
                        return true; // element became invisible
                    }
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    // If the element is NOT found or stale, it's effectively invisible
                    return true;
                }

                // Try again after refreshing the page
                driver.navigate().refresh();
                Thread.sleep(500);
            }

            return false; // timeout reached
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            // Restore original implicit wait
            driver.manage().timeouts().implicitlyWait(originalImplicitWait);
        }
    }


    public boolean refreshUntilVisible(By locator, int timeoutSeconds) {
        // store original implicit wait
        Duration originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();

        try {
            // decrease implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

            long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000);

            while (System.currentTimeMillis() < endTime) {
                try {
                    WebElement element = driver.findElement(locator);
                    if (element.isDisplayed()) {
                        return true;
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ignored) {
                    // ignore
                }

                driver.navigate().refresh();
                Thread.sleep(500);
            }

            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            // restore original implicit wait
            driver.manage().timeouts().implicitlyWait(originalImplicitWait);
        }
    }


    public void safelyAcceptAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            System.out.println("Optional alert was present and handled.");
        } catch (TimeoutException e) {
            // No alert — continue test
            System.out.println("No alert present, continuing...");
        } catch (NoAlertPresentException ignored) {
            // Alert vanished too fast — ignore
        }
    }

    public boolean isEnabled(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void clearInput(By locator) {
        //   driver.findElement(locator).click();
        driver.findElement(locator).sendKeys(Keys.CONTROL + "a");
        driver.findElement(locator).sendKeys(Keys.DELETE);
    }

    public void sendKeys(By locator, String text) {
        driver.findElement(locator).sendKeys(text);
    }

    public static String getFutureDateByDays(int days, String pattern) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return futureDate.format(formatter);
    }

    public void sendKeys(By locator, Keys keys) {
        driver.findElement(locator).sendKeys(keys);
    }

    public void sendKeysByActions(By locator, String textToSend) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(locator)).perform();
        driver.findElement(locator).sendKeys(textToSend);
    }

    public String getPastBusinessDate(int days) {
        LocalDate date = LocalDate.now().minusDays(days);

        // Generate holidays for the current year dynamically
        Set<LocalDate> holidays = getFederalHolidays(LocalDate.now().getYear());

        // Move backward until it's not weekend or holiday
        while (isWeekend(date) || holidays.contains(date)) {
            date = date.minusDays(1);
        }

        return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private Set<LocalDate> getFederalHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();

        // Fixed-date holidays
        holidays.add(LocalDate.of(year, 1, 1));   // New Year's Day
        holidays.add(LocalDate.of(year, 6, 19));  // Juneteenth
        holidays.add(LocalDate.of(year, 7, 4));   // Independence Day
        holidays.add(LocalDate.of(year, 11, 11)); // Veterans Day
        holidays.add(LocalDate.of(year, 12, 25)); // Christmas Day

        // Floating holidays
        holidays.add(getNthWeekdayOfMonth(year, Month.JANUARY, DayOfWeek.MONDAY, 3));  // MLK Day (3rd Mon)
        holidays.add(getNthWeekdayOfMonth(year, Month.FEBRUARY, DayOfWeek.MONDAY, 3)); // Presidents Day (3rd Mon)
        holidays.add(getLastWeekdayOfMonth(year, Month.MAY, DayOfWeek.MONDAY));        // Memorial Day (Last Mon)
        holidays.add(getFirstWeekdayOfMonth(year, Month.SEPTEMBER, DayOfWeek.MONDAY)); // Labor Day (1st Mon)
        holidays.add(getSecondWeekdayOfMonth(year, Month.OCTOBER, DayOfWeek.MONDAY));  // Columbus Day (2nd Mon)
        holidays.add(getNthWeekdayOfMonth(year, Month.NOVEMBER, DayOfWeek.THURSDAY, 4)); // Thanksgiving (4th Thu)

        return holidays;
    }

    private LocalDate getNthWeekdayOfMonth(int year, Month month, DayOfWeek dayOfWeek, int nth) {
        LocalDate date = LocalDate.of(year, month, 1);

        int count = 0;
        while (date.getMonth() == month) {
            if (date.getDayOfWeek() == dayOfWeek) {
                count++;
                if (count == nth) {
                    return date;
                }
            }
            date = date.plusDays(1);
        }
        return null;
    }

    private LocalDate getFirstWeekdayOfMonth(int year, Month month, DayOfWeek dayOfWeek) {
        return getNthWeekdayOfMonth(year, month, dayOfWeek, 1);
    }

    private LocalDate getSecondWeekdayOfMonth(int year, Month month, DayOfWeek dayOfWeek) {
        return getNthWeekdayOfMonth(year, month, dayOfWeek, 2);
    }

    private LocalDate getLastWeekdayOfMonth(int year, Month month, DayOfWeek dayOfWeek) {
        LocalDate date = LocalDate.of(year, month, month.length(Year.isLeap(year)));

        while (date.getMonth() == month) {
            if (date.getDayOfWeek() == dayOfWeek) {
                return date;
            }
            date = date.minusDays(1);
        }
        return null;
    }


    public void selectFromDropdown(By locator, String selectionType, String selectionValue) {

        // If the incoming value looks like a date (e.g., 01/20/2025)
        if (selectionValue.matches("\\d{2}/\\d{2}/\\d{4}")) {
            // Extract MM
            String month = selectionValue.substring(0, 2);

            // Convert MM to English month name
            selectionValue = convertMonthNumberToName(month);

            // Force selection type to text because month dropdown uses visible text
            selectionType = "text";
        }

        WebElement element = driver.findElement(locator);
        Select select = new Select(element);

        switch (selectionType.toLowerCase()) {
            case "text":
                select.selectByVisibleText(selectionValue);
                break;
            case "value":
                select.selectByValue(selectionValue);
                break;
            case "index":
                try {
                    int index = Integer.parseInt(selectionValue);
                    select.selectByIndex(index);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid index: " + selectionValue);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid selection type: " + selectionType +
                        ". Use 'text', 'value', or 'index'.");
        }
    }

    private String convertMonthNumberToName(String month) {
        switch (month) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "March";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                throw new IllegalArgumentException("Invalid month number: " + month);
        }
    }


    public void oneMACCalendarHandler(String day) {
        day = extractDay(day);
        // Normalize the input date to handle leading zeros
        // "01" -> "1", "09" -> "9", "10" stays "10"
        String normalizedDate = day.startsWith("0") ? day.substring(1) : day;

        List<WebElement> days = driver.findElements(By.xpath("//tbody/tr/td/button"));

        for (WebElement dayElement : days) {
            String dayText = dayElement.getText().trim();

            // Compare against BOTH formats just to be safe
            if (dayText.equalsIgnoreCase(day) || dayText.equalsIgnoreCase(normalizedDate)) {
                new Actions(driver).moveToElement(dayElement).click().perform();
                return;
            }
        }

        throw new NoSuchElementException("Calendar day not found for: " + day);
    }

    public void uploadFileAndCommit(By locator, String filePath) {
        // Step 1: trigger the upload (this replaces the original <input>)
        uploadFile(filePath, locator);
        // Step 2: re-locate the element AFTER upload to avoid stale reference
        WebElement el = wait.until(driver -> driver.findElement(locator));
        // Step 3: fire change + blur to commit validation
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }))", el);
        js.executeScript("arguments[0].dispatchEvent(new Event('blur', { bubbles: true }))", el);
    }

    public String getStateCode(String input) {
        if (input == null || !input.contains("-")) {
            return null;
        }

        return input.substring(0, input.indexOf('-'));
    }

    public String removeStateCode(String input) {
        if (input == null || !input.contains("-")) {
            return input;
        }

        return input.substring(input.indexOf('-') + 1);
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


    public void javaScriptClicker(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].click();", element);
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

}
