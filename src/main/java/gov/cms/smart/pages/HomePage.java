package gov.cms.smart.pages;

import gov.cms.smart.utils.UIElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private WebDriver driver;
    private UIElementUtils utils;
    By waiversSpas = By.xpath("//span[text()=\"SPAs or Waivers\"]/parent::a");

    public HomePage(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }

    public void goToSpasWaiversPage(){
        utils.javaScriptClicker(waiversSpas);
    }

}
