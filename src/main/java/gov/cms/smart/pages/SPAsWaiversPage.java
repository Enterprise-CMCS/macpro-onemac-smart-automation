package gov.cms.smart.pages;

import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.ExcelPackageSelector;
import gov.cms.smart.utils.PageFactory;
import gov.cms.smart.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class SPAsWaiversPage {

    private static final By NEW_BUTTON = By.xpath("//div[contains(@class,\"actionsWrapper\")]");
    private static final By SEARCH_INPUT = By.xpath("//lst-list-view-manager-settings-menu/parent::div/..//force-list-view-manager-search-bar//input");

    private final WebDriver driver;
    private final UIElementUtils utils;

    public SPAsWaiversPage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public NewSPAPage clickNew() {
        utils.clickElement(NEW_BUTTON);
        return PageFactory.getSPAPage(driver, utils);
    }

    public SpaPackage openExistingRecord(String state, String authority) {
        SpaPackage spa = ExcelPackageSelector.selectSpa(state, authority, "");
        utils.sendKeys(SEARCH_INPUT, spa.getPackageId());
        utils.sendKeys(SEARCH_INPUT, Keys.ENTER);
        utils.openRecord(spa.getPackageId());
        return spa;
    }


}
