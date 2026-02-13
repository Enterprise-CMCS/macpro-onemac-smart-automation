package gov.cms.smart.utils.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            extent = new ExtentReports();
            // 1. Setup HTML Report (Spark)
            ExtentSparkReporter spark = new ExtentSparkReporter("extent-report/OneMAC-SMART-TestReport.html");
            spark.config().setReportName("OneMAC SMART Automated Test Report");
            spark.config().setDocumentTitle("Test Results");
            // 2. Setup JSON Report
            JsonFormatter json = new JsonFormatter("extent-report/OneMAC-SMART-TestReport.json");
            // 3. Attach both reporters
            extent.attachReporter(spark, json);
        }
        return extent;
    }
}
