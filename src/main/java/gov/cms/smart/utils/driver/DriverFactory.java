package gov.cms.smart.utils.driver;

import gov.cms.smart.utils.config.TestContext;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver createDriver() {
        // Read system properties first (from GitHub Actions), fallback to ConfigReader
      /*  System.setProperty("webdriver.http.factory", "jdk-http-client");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\57901\\Desktop\\chromedriver\\chromedriver\\chromedriver.exe");
        WebDriver driver;
        ChromeOptions chromeOptions;
        chromeOptions = new ChromeOptions();
      // chromeOptions.addArguments("--headless=new");
      // chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.setBinary("C:\\Users\\57901\\Desktop\\chrome-win64\\chrome-win64\\chrome.exe");
        driver = new ChromeDriver(chromeOptions);*/

        String projectRoot = System.getProperty("user.dir");

        // ---------- Browser prefs ----------
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", projectRoot);
        prefs.put("download.prompt_for_download", false);
        prefs.put("safebrowsing.enabled", true);

        // ---------- Get browser & headless from TestContext ----------
        String browser = TestContext.browser();
        boolean isHeadless = TestContext.headless();

        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", prefs);

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }

                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");

                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (isHeadless) {
                    firefoxOptions.addArguments("--headless=new");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }

                firefoxOptions.addArguments("--no-sandbox");
                firefoxOptions.addArguments("--disable-dev-shm-usage");
                firefoxOptions.addArguments("--disable-gpu");

                driver = new FirefoxDriver(firefoxOptions);
                if (!isHeadless) driver.manage().window().maximize();
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        return driver;
    }

}