package gov.cms.smart.utils;

import java.util.List;

public class PackagePoolGenerator {

    private static final String FY = "25";

    public static void generate(List<String> states) {
        ExcelPackageTracker.ensureSheet();

        for (String s : states) {
            for (int i = 0; i < 6; i++) {
                String auth = (i % 2 == 0) ? "Medicaid SPA" : "CHIP SPA";
                String id = SpaIdGenerator.nextSpa(s, FY);
                ExcelPackageTracker.appendNewPackage("SPA", s, auth, "", id, "", "");
            }
        }

        for (String s : states) {
            for (int i = 0; i < 6; i++) {
                String auth = (i % 2 == 0) ? "1915(b)" : "1915(c)";
                String id = WaiverIdGenerator.nextInitial(s);
                ExcelPackageTracker.appendNewPackage("Waiver", s, auth, "Initial", id, "", "");
            }
        }
    }
}
