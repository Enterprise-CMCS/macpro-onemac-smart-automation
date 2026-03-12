package gov.cms.smart.utils.driver;


import gov.cms.smart.pages.*;
import gov.cms.smart.utils.ui.UIElementUtils;
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

    public static DetailsTab getSpaDetailsPage(WebDriver driver, UIElementUtils ui) {
        return new DetailsTab(driver, ui);
    }

    public static ReviewTab getReviewTab(WebDriver driver, UIElementUtils ui) {
        return new ReviewTab(driver, ui);
    }

    public static AdjudicationTab getAdjudicationTab(WebDriver driver, UIElementUtils ui) {
        return new AdjudicationTab(driver, ui);
    }
    public static NewSPAPage getNewSPAPage(WebDriver driver, UIElementUtils ui) {
        return new NewSPAPage(driver, ui);
    }

}
