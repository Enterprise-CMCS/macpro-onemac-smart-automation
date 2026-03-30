package gov.cms.smart.base;

import com.deque.html.axecore.results.Results;
import gov.cms.smart.utils.accessibility.AccessibilityUtils;
import gov.cms.smart.utils.assertions.AccessibilityAssert;

public class BaseAccessibilityTest extends BaseTest {

    protected final AccessibilityUtils accessibilityUtils = new AccessibilityUtils();

    protected void runAccessibilityCheck() {
        getUtils().waitForSalesforceLoading(getDriver());

        Results results = accessibilityUtils.analyzePage(getDriver());

        System.out.println(accessibilityUtils.buildIgnoredViolationMessage(results));
        AccessibilityAssert.assertNoAccessibilityViolations(results);
    }

    protected void runAccessibilityCheck(String cssSelector) {
        getUtils().waitForSalesforceLoading(getDriver());

        Results results = accessibilityUtils.analyzeSection(getDriver(), cssSelector);

        System.out.println(accessibilityUtils.buildIgnoredViolationMessage(results));
        AccessibilityAssert.assertNoAccessibilityViolations(results);
    }
}