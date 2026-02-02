package gov.cms.smart.utils;


import gov.cms.smart.models.SpaPackage;
import gov.cms.smart.models.WaiverPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExcelPackageSelector {

    private static final String FILE = resolveFilePath();
    private static final String SHEET = "Packages";
    private static final Random RAND = new Random();

    private static String resolveFilePath() {
        String runOn = ConfigReader.get("runOn");

        if (runOn == null || runOn.isBlank()) {
            throw new RuntimeException("runOn not set in config.properties");
        }

        String key = "packages." + runOn.toLowerCase();
        String path = ConfigReader.get(key);

        if (path == null || path.isBlank()) {
            throw new RuntimeException("No packages file configured for runOn=" + runOn);
        }

        return path;
    }




    private static List<Row> read() {
        List<Row> result = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(SHEET);
            if (sheet == null) return result;

            int last = sheet.getLastRowNum();

            for (int i = 1; i <= last; i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row)) continue;
                result.add(row);
            }

        } catch (Exception e) {
            throw new RuntimeException("Excel read error", e);
        }

        return result;
    }


    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;

        for (int c = 0; c <= 6; c++) { // Only inspect meaningful columns
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String val = cell.toString().trim();
                if (!val.isEmpty()) return false;
            }
        }

        return true;
    }

    private static Row pick(List<Row> rows) {
        return rows.isEmpty() ? null : rows.get(RAND.nextInt(rows.size()));
    }

    private static String get(Row r, int col) {
        if (r == null) return "";
        Cell c = r.getCell(col);
        if (c == null) return "";
        return c.toString().trim();
    }

    private static SpaPackage toSpa(Row r) {
        SpaPackage s = new SpaPackage();
        s.setState(get(r, 1));
        s.setAuthority(get(r, 2));
        s.setPackageId(get(r, 4));
        s.setStatus(get(r, 5));
        return s;
    }

    private static WaiverPackage toWaiver(Row r) {
        WaiverPackage w = new WaiverPackage();
        w.setState(get(r, 1));
        w.setAuthority(get(r, 2));
        w.setActionType(get(r, 3));
        w.setPackageId(get(r, 4));
        w.setStatus(get(r, 5));
        w.setParentId(get(r, 6));
        return w;
    }

    private static List<Row> match(String type, String state, String authority,
                                   String actionType, String status) {

        List<Row> all = read();
        List<Row> out = new ArrayList<>();

        for (Row r : all) {
            if (type.equalsIgnoreCase(get(r, 0)) &&
                    state.equalsIgnoreCase(get(r, 1)) &&
                    authority.equalsIgnoreCase(get(r, 2)) &&
                    (actionType == null || actionType.equalsIgnoreCase(get(r, 3))) &&
                    status.equalsIgnoreCase(get(r, 5))) {
                out.add(r);
            }
        }

        return out;
    }

    public static SpaPackage selectSpa(String state, String authority, String status) {
        Row r = pick(match("SPA", state, authority, null, status));
        if (r == null) {
            throw new RuntimeException(
                    "No SPA found for: " + state + " | " + authority + " | " + status
            );
        }
        return toSpa(r);
    }

    public static WaiverPackage selectWaiver(
            String state, String authority, String actionType, String status) {

        Row r = pick(match("Waiver", state, authority, actionType, status));

        if (r == null) {
            throw new RuntimeException(
                    "No Waiver found for: " + state + " | " + authority +
                            " | " + actionType + " | " + status
            );
        }

        return toWaiver(r);
    }


}
