package gov.cms.smart.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExcelPackageTracker {

    // ==== Defaults you approved ====
    private static final String FILE = "src/test/resources/packages.xlsx";
    private static final String SHEET = "Packages";

    // Columns
    private static final int COL_TYPE   = 0; // SPA | Waiver
    private static final int COL_STATE  = 1; // e.g., MD
    private static final int COL_AUTH   = 2; // Medicaid SPA | CHIP SPA | 1915(b) | 1915(c)
    private static final int COL_ACTION = 3; // "" (SPA) | Initial | Renewal | Temporary Extension | Amendment
    private static final int COL_ID     = 4; // PackageId
    private static final int COL_STATUS = 5; // "", Approved, ...
    private static final int COL_PARENT = 6; // parent PackageId (for Renewal/TE/Amendment)

    // Patterns
    private static final Pattern RENEWAL_OR_INITIAL = Pattern.compile("^[A-Z]{2}-\\d{4,5}\\.R\\d{2}\\.\\d{2}$");
    private static final Pattern TEMP_EXT           = Pattern.compile("^[A-Z]{2}-\\d{4,5}\\.R\\d{2}\\.TE\\d{2}$");
    private static final Pattern AMENDMENT          = Pattern.compile("^[A-Z]{2}-\\d{4,5}\\.R\\d{2}\\.\\d{2}$"); // same surface as Initial/Renewal; we rely on ActionType to tell

    // =============== Public API =================

    /** Ensure workbook & header exist. Safe to call before any read/write. */
    public static void ensureSheet() {
        try {
            File f = new File(FILE);
            if (!f.exists()) {
                try (Workbook wb = new XSSFWorkbook()) {
                    Sheet sh = wb.createSheet(SHEET);
                    Row h = sh.createRow(0);
                    h.createCell(COL_TYPE).setCellValue("PackageType");
                    h.createCell(COL_STATE).setCellValue("State");
                    h.createCell(COL_AUTH).setCellValue("Authority");
                    h.createCell(COL_ACTION).setCellValue("ActionType");
                    h.createCell(COL_ID).setCellValue("PackageID");
                    h.createCell(COL_STATUS).setCellValue("Status");
                    h.createCell(COL_PARENT).setCellValue("ParentID");
                    try (FileOutputStream fos = new FileOutputStream(FILE)) {
                        wb.write(fos);
                    }
                }
            } else {
                // Make sure the sheet exists
                try (FileInputStream fis = new FileInputStream(FILE);
                     Workbook wb = new XSSFWorkbook(fis)) {
                    if (wb.getSheet(SHEET) == null) {
                        wb.createSheet(SHEET);
                        Row h = wb.getSheet(SHEET).createRow(0);
                        h.createCell(COL_TYPE).setCellValue("PackageType");
                        h.createCell(COL_STATE).setCellValue("State");
                        h.createCell(COL_AUTH).setCellValue("Authority");
                        h.createCell(COL_ACTION).setCellValue("ActionType");
                        h.createCell(COL_ID).setCellValue("PackageID");
                        h.createCell(COL_STATUS).setCellValue("Status");
                        h.createCell(COL_PARENT).setCellValue("ParentID");
                        try (FileOutputStream fos = new FileOutputStream(FILE)) {
                            wb.write(fos);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to ensure Excel sheet", e);
        }
    }

    /** Append a new row. PackageType must be "SPA" or "Waiver". */
    public static void appendNewPackage(String packageType, String state, String authority,
                                        String actionTypeOrNull, String packageId, String status,
                                        String parentId) {
        ensureSheet();
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sh = wb.getSheet(SHEET);

            // prevent duplicates (by PackageID)
            if (findRowIndexById(sh, packageId) != -1) {
                // If you prefer to throw instead of ignoring, replace with:
                // throw new IllegalStateException("Duplicate PackageID: " + packageId);
                return;
            }

            Row r = sh.createRow(sh.getLastRowNum() + 1);
            r.createCell(COL_TYPE).setCellValue(safe(packageType));
            r.createCell(COL_STATE).setCellValue(safe(state));
            r.createCell(COL_AUTH).setCellValue(safe(authority));

            if ("Waiver".equalsIgnoreCase(packageType)) {
                r.createCell(COL_ACTION).setCellValue(actionTypeOrNull == null ? "" : actionTypeOrNull);
            } else {
                r.createCell(COL_ACTION).setCellValue("");
            }

            r.createCell(COL_ID).setCellValue(safe(packageId));
            r.createCell(COL_STATUS).setCellValue(status == null ? "" : status.trim());
            r.createCell(COL_PARENT).setCellValue(parentId == null ? "" : parentId.trim());

            try (FileOutputStream fos = new FileOutputStream(FILE)) {
                wb.write(fos);
            }

        } catch (IOException e) {
            throw new RuntimeException("Excel write error", e);
        }
    }

    /** Update status for a given PackageId (e.g., set to "Approved" after Seatool step). */
    public static void updateStatus(String packageId, String newStatus) {
        ensureSheet();
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            int idx = findRowIndexById(sh, packageId);
            if (idx == -1) throw new IllegalArgumentException("Package not found: " + packageId);
            Row r = sh.getRow(idx);
            cell(r, COL_STATUS).setCellValue(newStatus == null ? "" : newStatus.trim());
            try (FileOutputStream fos = new FileOutputStream(FILE)) {
                wb.write(fos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel update error", e);
        }
    }

    /** Remove a row by PackageId and shift rows up to keep the sheet compact. */
    public static void removePackageById(String packageId) {
        ensureSheet();
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            int idx = findRowIndexById(sh, packageId);
            if (idx == -1) return;
            int last = sh.getLastRowNum();
            if (idx >= 0 && idx < last) {
                sh.shiftRows(idx + 1, last, -1);
            } else {
                Row r = sh.getRow(idx);
                if (r != null) sh.removeRow(r);
            }
            try (FileOutputStream fos = new FileOutputStream(FILE)) {
                wb.write(fos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel delete error", e);
        }
    }

    /** Read all PackageIDs for a state (any type). */
    public static List<String> readAllIds(String state) {
        ensureSheet();
        List<String> out = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            for (Row r : sh) {
                if (r.getRowNum() == 0) continue;
                if (ieq(getStr(r, COL_STATE), state)) {
                    String id = getStr(r, COL_ID);
                    if (!id.isEmpty()) out.add(id);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel read error", e);
        }
        return out;
    }

    /** Read all Waiver IDs for a state (excludes SPA). */
    public static List<String> readWaiverIds(String state) {
        ensureSheet();
        List<String> out = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            for (Row r : sh) {
                if (r.getRowNum() == 0) continue;
                if (!"Waiver".equalsIgnoreCase(getStr(r, COL_TYPE))) continue;
                if (!ieq(getStr(r, COL_STATE), state)) continue;
                String id = getStr(r, COL_ID);
                if (!id.isEmpty()) out.add(id);
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel read error", e);
        }
        return out;
    }

    // ---------- ID helpers (derive next IDs by scanning Excel) ----------

    /** Next SPA ID → SS-YY-#### (purely by max suffix per state/year). */
    public static String nextSpaId(String state, String fiscalYear) {
        ensureSheet();
        int max = 8999;
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            for (Row r : sh) {
                if (r.getRowNum() == 0) continue;
                if (!"SPA".equalsIgnoreCase(getStr(r, COL_TYPE))) continue;
                if (!ieq(getStr(r, COL_STATE), state)) continue;
                String id = getStr(r, COL_ID); // e.g., MD-25-9001
                String[] parts = id.split("-");
                if (parts.length == 3 && parts[1].equals(fiscalYear)) {
                    try {
                        int n = Integer.parseInt(parts[2]);
                        if (n > max) max = n;
                    } catch (NumberFormatException ignore) {}
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel read error", e);
        }
        return String.format("%s-%s-%04d", state, fiscalYear, max + 1);
    }

    /** Next Initial Waiver base → SS-####.R00.00 (or SS-#####...) */
    public static String nextInitialWaiverId(String state, int minBase) {
        ensureSheet();
        int maxBase = Math.max(minBase - 1, 0);
        try (FileInputStream fis = new FileInputStream(FILE);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sh = wb.getSheet(SHEET);
            for (Row r : sh) {
                if (r.getRowNum() == 0) continue;
                if (!"Waiver".equalsIgnoreCase(getStr(r, COL_TYPE))) continue;
                if (!ieq(getStr(r, COL_STATE), state)) continue;
                String id = getStr(r, COL_ID); // e.g., MD-2200.R00.00
                String[] p0 = id.split("\\.");
                if (p0.length >= 1) {
                    String[] pBase = p0[0].split("-");
                    if (pBase.length == 2) {
                        try {
                            int base = Integer.parseInt(pBase[1]);
                            if (base > maxBase) maxBase = base;
                        } catch (NumberFormatException ignore) {}
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Excel read error", e);
        }
        return String.format("%s-%d.R00.00", state, maxBase + 1);
    }

    /** Next Renewal from an APPROVED parent (Initial or Renewal): bump R, reset M=00. */
    public static String nextRenewalFromParent(String parentWaiverId) {
        // parent must match SS-####.R##.MM (MM can be 00.. or amendment)
        String[] parts = parentWaiverId.split("\\.");
        if (parts.length < 2) throw new IllegalArgumentException("Invalid parent: " + parentWaiverId);
        String base = parts[0]; // SS-####
        String rPart = parts[1]; // R##
        int r = parseIntSafe(rPart.substring(1), 0);
        return base + ".R" + String.format("%02d", (r + 1)) + ".00";
    }

    /** Next Amendment under the same base & R: SS-####.R##.## (## increments). */
    public static String nextAmendmentFromParent(String parentWaiverId) {
        // parent can be Initial (R00.00) or Renewal (Rnn.00)
        String[] parts = parentWaiverId.split("\\.");
        if (parts.length < 2) throw new IllegalArgumentException("Invalid parent: " + parentWaiverId);
        String base = parts[0]; // SS-####
        String rPart = parts[1]; // R##
        String prefix = base + "." + rPart + ".";
        int maxAmend = 0;

        for (String id : readAllIds(base.substring(0, 2))) {
            if (!id.startsWith(prefix)) continue;           // same base + R
            if (id.contains(".TE")) continue;               // skip TEs
            String[] p = id.split("\\.");
            if (p.length == 3) {
                String mm = p[2];                           // ## portion
                if (mm.matches("\\d{2}")) {
                    int m = parseIntSafe(mm, 0);
                    if (m > maxAmend) maxAmend = m;
                }
            }
        }
        return prefix + String.format("%02d", maxAmend + 1);
    }

    /** Next Temporary Extension: SS-####.R##.TE## (## increments under same base+R). */
    public static String nextTemporaryExtensionId(String parentWaiverId) {
        String[] parts = parentWaiverId.split("\\.");
        if (parts.length < 2) throw new IllegalArgumentException("Invalid parent: " + parentWaiverId);
        String base = parts[0]; // SS-####
        String rPart = parts[1]; // R##
        String prefix = base + "." + rPart + ".TE";
        int maxTe = 0;

        for (String id : readAllIds(base.substring(0, 2))) {
            if (!id.startsWith(prefix)) continue; // same base + R
            String[] p = id.split("\\.");
            if (p.length == 3 && p[2].startsWith("TE")) {
                String num = p[2].substring(2); // after TE
                if (num.matches("\\d{2}")) {
                    int t = parseIntSafe(num, 0);
                    if (t > maxTe) maxTe = t;
                }
            }
        }
        return prefix + String.format("%02d", maxTe + 1);
    }

    // =============== Private helpers =================

    private static int findRowIndexById(Sheet sh, String packageId) {
        for (Row r : sh) {
            if (r.getRowNum() == 0) continue;
            if (ieq(getStr(r, COL_ID), packageId)) return r.getRowNum();
        }
        return -1;
    }

    private static String getStr(Row r, int col) {
        if (r == null) return "";
        Cell c = r.getCell(col);
        if (c == null) return "";
        if (c.getCellType() == CellType.NUMERIC) {
            // Avoid scientific notation; cast to plain int if safe, else to long/double string
            double d = c.getNumericCellValue();
            long l = (long) d;
            if (Math.abs(d - l) < 1e-9) return String.valueOf(l);
            return String.valueOf(d);
        }
        if (c.getCellType() == CellType.BOOLEAN) {
            return Boolean.toString(c.getBooleanCellValue());
        }
        c.setCellType(CellType.STRING);
        return c.getStringCellValue() == null ? "" : c.getStringCellValue().trim();
    }

    private static Cell cell(Row r, int col) {
        Cell c = r.getCell(col);
        if (c == null) c = r.createCell(col);
        return c;
    }

    private static boolean ieq(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
