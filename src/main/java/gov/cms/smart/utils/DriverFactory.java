package gov.cms.smart.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver createDriver() {
        WebDriver driver;
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("linux")) {
            // Path for GitHub Actions ARM runner
            chromeOptions.setBinary("/usr/bin/chromium"); // safer than chromium-browser
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        } else {
            // Local Windows/Mac — let Selenium find your installed Chrome automatically
        }

        driver = new ChromeDriver(chromeOptions);
        return driver;
    }

}