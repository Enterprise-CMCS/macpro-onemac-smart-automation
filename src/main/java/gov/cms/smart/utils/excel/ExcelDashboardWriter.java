package gov.cms.smart.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;

public class ExcelDashboardWriter {

    /**
     * Updates the dashboard table counts:
     * A: Functional Area
     * B: Total Automated Tests
     * C: Passed
     * D: Failed
     * E: Blocked
     *
     * Leaves % columns untouched (they should be formulas).
     */
    public static void updateTestCoverageDashboard(
            String filePath,
            String sheetName,
            Map<String, ? extends CountsView> areaCounts
    ) throws IOException {

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Excel dashboard not found at: " + file.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            // Your table starts at row 2 in Excel UI => index 1 (0-based)
            // Column A => index 0
            int startRowIndex = 1;
            int areaCol = 0;

            for (Map.Entry<String, ? extends CountsView> entry : areaCounts.entrySet()) {
                String area = entry.getKey();
                CountsView c = entry.getValue();

                Row row = findRowByArea(sheet, startRowIndex, areaCol, area);
                if (row == null) {
                    // If not found, skip (or you can insert new rows if you want)
                    continue;
                }

                int passed = c.getPassed();
                int failed = c.getFailed();
                int blocked = c.getBlocked();
                int total = passed + failed + blocked;

                setInt(row, 1, total);   // B
                setInt(row, 2, passed);  // C
                setInt(row, 3, failed);  // D
                setInt(row, 4, blocked); // E

                // Do NOT overwrite F/G/H (percent formulas)
            }

            // Optional: evaluate formulas so the file opens already updated
            wb.getCreationHelper().createFormulaEvaluator().evaluateAll();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    private static Row findRowByArea(Sheet sheet, int startRowIndex, int areaCol, String area) {
        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            if (r == null) continue;

            Cell cell = r.getCell(areaCol);
            if (cell == null) continue;

            if (cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue().trim();
                if (val.equalsIgnoreCase(area.trim())) {
                    return r;
                }
            }
        }
        return null;
    }

    private static void setInt(Row row, int colIndex, int value) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) cell = row.createCell(colIndex);
        cell.setCellValue(value);
    }

    /**
     * Small interface so your listener can pass counts without exposing internals.
     */
    public interface CountsView {
        int getPassed();
        int getFailed();
        int getBlocked();
    }
}