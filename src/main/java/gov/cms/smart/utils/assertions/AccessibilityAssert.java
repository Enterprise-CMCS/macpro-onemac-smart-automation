package gov.cms.smart.utils.assertions;

import com.deque.html.axecore.results.Results;
import gov.cms.smart.utils.accessibility.AccessibilityUtils;
import org.testng.Assert;

public class AccessibilityAssert {

    private static final AccessibilityUtils accessibilityUtils = new AccessibilityUtils();

    public static void assertNoAccessibilityViolations(Results results) {
        Assert.assertTrue(
                accessibilityUtils.hasNoActionableViolations(results),
                accessibilityUtils.buildActionableViolationMessage(results)
        );
    }
}