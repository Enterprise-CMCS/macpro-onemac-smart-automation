package gov.cms.smart.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StateCounterTracker {

    private static final String FILE = resolveFilePath();
    private static final String SHEET = "StateCounters";

    private static final int COL_STATE = 0;
    private static final int COL_SPA = 1;
    private static final int COL_WAIVER = 2;
    private static final int COL_RENEWAL = 3;
    private static final int COL_AMEND = 4;
    private static final int COL_TE = 5;

    private static String resolveFilePath() {
        String runOn = ConfigReader.get("runOn");

        if (runOn == null || runOn.isBlank()) {
            throw new RuntimeException("runOn not set in config.properties");
        }

        String key = "stateCounters." + runOn.toLowerCase();
        String path = ConfigReader.get(key);

        if (path == null || path.isBlank()) {
            throw new RuntimeException("No state counter file configured for runOn=" + runOn);
        }

        return path;
    }

    private static Row findRow(Sheet sh, String state) {
        for (Row r : sh) {
            if (r.getRowNum() == 0) continue;
            if (r.getCell(COL_STATE).getStringCellValue().equalsIgnoreCase(state)) {
                return r;
            }
        }
        throw new RuntimeException("State not found in counters sheet: " + state);
    }

    private static Workbook openWorkbook() throws IOException {
        FileInputStream fis = new FileInputStream(FILE);
        return new XSSFWorkbook(fis);
    }

    private static void saveWorkbook(Workbook wb) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FILE)) {
            wb.write(fos);
        }
    }

    public static synchronized int nextSpaNumber(String state) {
        try (Workbook wb = openWorkbook()) {
            Sheet sh = wb.getSheet(SHEET);
            Row r = findRow(sh, state);
            int current = (int) r.getCell(COL_SPA).getNumericCellValue();
            r.getCell(COL_SPA).setCellValue(current + 1);
            saveWorkbook(wb);
            return current;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized int nextWaiverBase(String state) {
        try (Workbook wb = openWorkbook()) {
            Sheet sh = wb.getSheet(SHEET);
            Row r = findRow(sh, state);
            int current = (int) r.getCell(COL_WAIVER).getNumericCellValue();
            r.getCell(COL_WAIVER).setCellValue(current + 1);
            saveWorkbook(wb);
            return current;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized int nextAmendmentIndex(String state) {
        try (Workbook wb = openWorkbook()) {
            Sheet sh = wb.getSheet(SHEET);
            Row r = findRow(sh, state);
            int current = (int) r.getCell(COL_AMEND).getNumericCellValue();
            r.getCell(COL_AMEND).setCellValue(current + 1);
            saveWorkbook(wb);
            return current;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized int nextTEIndex(String state) {
        try (Workbook wb = openWorkbook()) {
            Sheet sh = wb.getSheet(SHEET);
            Row r = findRow(sh, state);
            int current = (int) r.getCell(COL_TE).getNumericCellValue();
            r.getCell(COL_TE).setCellValue(current + 1);
            saveWorkbook(wb);
            return current;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
