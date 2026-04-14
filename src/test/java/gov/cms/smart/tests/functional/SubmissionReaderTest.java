package gov.cms.smart.tests.functional;

import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.utils.excel.ExcelPackageSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class SubmissionReaderTest {

    private static final Logger logger = LogManager.getLogger();

    @Test
    public void excelSubmissionReadingTest(){
        SpaPackage spaPackage = ExcelPackageSelector.selectSpa("AL", "Medicaid SPA", "");
        logger.info("Read the following SPA Package from Excel sheet: {}", spaPackage.getPackageId());
    }
}
