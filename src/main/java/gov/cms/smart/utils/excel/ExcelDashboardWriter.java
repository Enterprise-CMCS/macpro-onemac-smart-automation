package gov.cms.smart.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
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
     * Also updates the execution timestamp at the top of the sheet.
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

            // Top banner row
            updateExecutionDateTime(sheet, wb, LocalDateTime.now());

            // Header is now on Excel row 2, data starts on Excel row 3 => index 2
            int startRowIndex = 2;
            int areaCol = 0;
            for (Map.Entry<String, ? extends CountsView> entry : areaCounts.entrySet()) {
                String area = entry.getKey();
                CountsView c = entry.getValue();
                Row row = findRowByArea(sheet, startRowIndex, areaCol, area);
                if (row == null) {
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

                // Leave F/G/H formulas untouched
            }

            wb.getCreationHelper().createFormulaEvaluator().evaluateAll();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    /**
     * Writes the execution date/time to the top row.
     *
     * Expected layout:
     * A1 = "Execution Date & Time:"
     * C1:D1 merged (optional) = timestamp value
     */
    private static void updateExecutionDateTime(Sheet sheet, Workbook wb, LocalDateTime dateTime) {
        Row row = getOrCreateRow(sheet, 0); // Excel row 1

        // A1 label
        Cell labelCell = getOrCreateCell(row, 0);
        labelCell.setCellValue("Execution Date & Time:");

        // Write datetime into C1 (column index 2)
        Cell valueCell = getOrCreateCell(row, 2);
        valueCell.setCellValue(java.sql.Timestamp.valueOf(dateTime));

        CellStyle dateTimeStyle = wb.createCellStyle();
        short format = wb.getCreationHelper()
                .createDataFormat()
                .getFormat("m/d/yyyy h:mm:ss AM/PM");
        dateTimeStyle.setDataFormat(format);
        valueCell.setCellStyle(dateTimeStyle);

      /*  // Optional: merge C1:D1
        mergeIfNeeded(sheet, 0, 0, 2, 3);*/
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

    private static Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return row != null ? row : sheet.createRow(rowIndex);
    }

    private static Cell getOrCreateCell(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return cell != null ? cell : row.createCell(colIndex);
    }

    private static void mergeIfNeeded(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.getFirstRow() == firstRow
                    && region.getLastRow() == lastRow
                    && region.getFirstColumn() == firstCol
                    && region.getLastColumn() == lastCol) {
                return;
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    public interface CountsView {
        int getPassed();
        int getFailed();
        int getBlocked();
    }
}