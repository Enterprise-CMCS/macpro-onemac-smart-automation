package gov.cms.smart.utils;

import gov.cms.smart.models.SpaPackage;

public class SpaGenerator {

    private static final String FY = "25"; // or pass in dynamically

    public static SpaPackage createSpa(String state, String authority) {
        // 1. Generate ID using the counter-based SPA generator
        String spaId = SpaIdGenerator.nextSpa(state, FY);
        // 2. Append to Excel
        ExcelPackageTracker.appendNewPackage(
                "SPA",
                state,
                authority,
                "",       // no action type for SPA
                spaId,
                "",       // status starts blank
                ""        // no parent
        );
        // 3. Return the SPA object
        SpaPackage spa = new SpaPackage();
        spa.setState(state);
        spa.setAuthority(authority);
        spa.setPackageId(spaId);
        spa.setStatus("");

        return spa;
    }
}
