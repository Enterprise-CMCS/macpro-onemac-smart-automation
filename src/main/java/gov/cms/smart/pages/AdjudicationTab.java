package gov.cms.smart.pages;

import gov.cms.smart.utils.ui.UIElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdjudicationTab {

    private final WebDriver driver;
    private final UIElementUtils utils;
    private static final Logger logger = LogManager.getLogger();
    public static final By DOCUMENTS_POSTED_TO_MEDICAID_GOV_LABEL = By.xpath("//span[text()=\"Documents posted to Medicaid.gov\"]");


    public AdjudicationTab(WebDriver driver, UIElementUtils utils) {
        this.driver = driver;
        this.utils = utils;
    }
}
