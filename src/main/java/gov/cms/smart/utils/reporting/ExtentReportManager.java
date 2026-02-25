package gov.cms.smart.utils.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            new java.io.File("extent-report").mkdirs();

            extent = new ExtentReports();
            ExtentSparkReporter spark =
                    new ExtentSparkReporter("extent-report/OneMAC-SMART-TestReport.html");
            JsonFormatter json =
                    new JsonFormatter("extent-report/OneMAC-SMART-TestReport.json");

            extent.attachReporter(spark, json);
        }
        return extent;
    }
}
