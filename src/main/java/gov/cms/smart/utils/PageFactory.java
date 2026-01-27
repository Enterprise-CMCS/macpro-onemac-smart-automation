package gov.cms.smart.utils;


import gov.cms.smart.pages.HomePage;
import gov.cms.smart.pages.LoginPage;
import gov.cms.smart.pages.SPAPage;
import gov.cms.smart.pages.SpaDetailsPage;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    public static HomePage getHomePage(WebDriver driver, UIElementUtils ui) {
        return new HomePage(driver, ui);
    }


    public static LoginPage getLoginPage(WebDriver driver, UIElementUtils ui) {
        return new LoginPage(driver, ui);
    }

    public static SpaDetailsPage getSpaDetailsPage(WebDriver driver, UIElementUtils ui) {
        return new SpaDetailsPage(driver, ui);
    }

    public static SPAPage getSPAPage(WebDriver driver, UIElementUtils ui) {
        return new SPAPage(driver, ui);
    }

}
