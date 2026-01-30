package gov.cms.smart.utils;


import gov.cms.smart.pages.*;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    public static HomePage getHomePage(WebDriver driver, UIElementUtils ui) {
        return new HomePage(driver, ui);
    }


    public static LoginPage getLoginPage(WebDriver driver, UIElementUtils ui) {
        return new LoginPage(driver, ui);
    }
    public static SPAsWaiversPage getSpaWaiversPage(WebDriver driver, UIElementUtils ui) {
        return new SPAsWaiversPage(driver, ui);
    }

    public static SpaDetailsPage getSpaDetailsPage(WebDriver driver, UIElementUtils ui) {
        return new SpaDetailsPage(driver, ui);
    }

    public static NewSPAPage getSPAPage(WebDriver driver, UIElementUtils ui) {
        return new NewSPAPage(driver, ui);
    }

}
