package gov.cms.smart.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import gov.cms.smart.base.BaseTest;
import gov.cms.smart.utils.config.ConfigReader;
import gov.cms.smart.utils.excel.ExcelDashboardWriter;
import gov.cms.smart.utils.reporting.ExtentReportManager;
import gov.cms.smart.utils.reporting.ScreenshotUtil;
import org.testng.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class TestListener  implements ITestListener, ISuiteListener {

    private static final ExtentReports extent = ExtentReportManager.getInstance();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static final String SHEET_NAME = "TestCoverage";
    private static final Map<String, Counts> AREA_COUNTS = new ConcurrentHashMap<>();

    private static class Counts implements ExcelDashboardWriter.CountsView {
        private final AtomicInteger passed = new AtomicInteger(0);
        private final AtomicInteger failed = new AtomicInteger(0);
        private final AtomicInteger blocked = new AtomicInteger(0);

        @Override public int getPassed() { return passed.get(); }
        @Override public int getFailed() { return failed.get(); }
        @Override public int getBlocked() { return blocked.get(); }
    }

    private static String getFunctionalArea(ITestResult result) {
        String[] groups = result.getMethod().getGroups();
        if (groups != null && groups.length > 0) return groups[0];
        return "Uncategorized";
    }

    private static Counts countsFor(ITestResult result) {
        return AREA_COUNTS.computeIfAbsent(getFunctionalArea(result), k -> new Counts());
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
            test.set(extentTest);
        } catch (Exception ignored) {
            // never let reporting break execution
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        countsFor(result).passed.incrementAndGet();
        try {
            ExtentTest t = test.get();
            if (t != null) t.pass("Test passed");
        } catch (Exception ignored) {}
    }

    @Override
    public void onTestFailure(ITestResult result) {
        //increment first (so dashboard is accurate even if screenshot fails)
        countsFor(result).failed.incrementAndGet();

        try {
            ExtentTest t = test.get();
            if (t != null) t.fail(result.getThrowable());
        } catch (Exception ignored) {}

        // screenshot should NEVER break listener
        try {
            if (BaseTest.getDriver() != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(BaseTest.getDriver(), result.getName());
                ExtentTest t = test.get();
                if (t != null && screenshotPath != null) {
                    t.addScreenCaptureFromPath(screenshotPath);
                }
            }
        } catch (Exception e) {
            // optionally log:
            System.err.println("Screenshot capture failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        countsFor(result).blocked.incrementAndGet();
        try {
            ExtentTest t = test.get();
            if (t != null) t.skip(result.getThrowable());
        } catch (Exception ignored) {}
    }

    /** Runs ONCE at the end of the entire suite. Perfect place to write Excel. */
    @Override
    public void onFinish(ISuite suite) {
        try {
            extent.flush();
        } catch (Exception e) {
            System.err.println("Extent flush failed: " + e.getMessage());
        }

        String runOn = ConfigReader.get("runOn");
        String dashboardPath = "src/test/resources/TestCoverageDashboard_" + runOn + ".xlsx";

        try {
            ExcelDashboardWriter.updateTestCoverageDashboard(dashboardPath, SHEET_NAME, AREA_COUNTS);
            System.out.println("Updated Excel dashboard: " + dashboardPath);
        } catch (Exception e) {
            System.err.println("Failed to update Excel dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
