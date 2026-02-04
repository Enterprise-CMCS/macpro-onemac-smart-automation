package gov.cms.smart.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();

// CI stability flags
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("linux")) {
            options.setBinary("/usr/bin/chromium");   // MUST be chromium, not chromium-browser
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        }

        WebDriver driver = new ChromeDriver(options);
        return driver;

    }

}