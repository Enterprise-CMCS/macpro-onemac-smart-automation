package gov.cms.smart.utils.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("extent-report/OneMAC-SMART-TestReport.html");
            spark.config().setReportName("OneMAC SMART Automated Test Report");
            spark.config().setDocumentTitle("Test Results");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}
