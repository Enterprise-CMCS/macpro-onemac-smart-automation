package gov.cms.smart.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class AssertionUtil {

    private static final Logger logger = LogManager.getLogger(AssertionUtil.class);

    // Thread-safe SoftAssert for parallel execution
    private static final ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    private AssertionUtil() {
        // prevent instantiation
    }



    public static void assertTrue(boolean condition, String message) {
        logger.info("ASSERT TRUE: " + message);
        Assert.assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition, String message) {
        logger.info("ASSERT FALSE: " + message);
        Assert.assertFalse(condition, message);
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        logger.info("ASSERT EQUALS: " + message + "\nExpected: " + expected + " | Actual: " + actual);
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertNotEquals(Object actual, Object expected, String message) {
        logger.info("ASSERT NOT EQUALS: " + message + "\nExpected NOT: " + expected + " | Actual: " + actual);
        Assert.assertNotEquals(actual, expected, message);
    }

    public static void fail(String message) {
        logger.error("ASSERT FAIL: " + message);
        Assert.fail(message);
    }


    public static void softAssertTrue(boolean condition, String message) {
        logger.info("SOFT ASSERT TRUE: " + message);
        softAssert.get().assertTrue(condition, message);
    }

    public static void softAssertFalse(boolean condition, String message) {
        logger.info("SOFT ASSERT FALSE: " + message);
        softAssert.get().assertFalse(condition, message);
    }

    public static void softAssertEquals(Object actual, Object expected, String message) {
        logger.info("SOFT ASSERT EQUALS: " + message + "\nExpected: " + expected + " | Actual: " + actual);
        softAssert.get().assertEquals(actual, expected, message);
    }

    public static void softAssertNotEquals(Object actual, Object expected, String message) {
        logger.info("SOFT ASSERT NOT EQUALS: " + message + "\nExpected NOT: " + expected + " | Actual: " + actual);
        softAssert.get().assertNotEquals(actual, expected, message);
    }

    public static void softFail(String message) {
        logger.error("SOFT ASSERT FAIL: " + message);
        softAssert.get().fail(message);
    }


    public static void assertAll() {
        logger.info("SOFT ASSERT: Executing assertAll()");
        try {
            softAssert.get().assertAll();
        } finally {
            // Clean up for next test (parallel safe)
            softAssert.remove();
        }
    }
}
