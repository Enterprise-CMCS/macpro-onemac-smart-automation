package gov.cms.smart.utils;

import java.util.List;

public class WaiverIdGenerator {

    public static String nextInitial(String state) {
        int base = StateCounterTracker.nextWaiverBase(state);
        return String.format("%s-%d.R00.00", state, base);
    }

    public static String nextRenewal(String parentId, List<String> existing) {
        // parentId example: MD-2212.R00.00
        String[] parts = parentId.split("\\.");
        String base = parts[0];           // MD-2212
        String rPart = parts[1];          // R00

        int currentRenewal = Integer.parseInt(rPart.substring(1)); // 0
        int nextRenewal = currentRenewal + 1;                      // 1

        String candidate = base + ".R" + String.format("%02d", nextRenewal) + ".00";

        // ensure uniqueness in Excel
        while (existing.contains(candidate)) {
            nextRenewal++;
            candidate = base + ".R" + String.format("%02d", nextRenewal) + ".00";
        }

        return candidate;
    }


    public static String nextAmendment(String parentId) {
        String[] parts = parentId.split("\\.");
        String prefix = parts[0] + "." + parts[1] + ".";
        int index = StateCounterTracker.nextAmendmentIndex(parts[0].substring(0,2));
        return prefix + String.format("%02d", index);
    }

    public static String nextTemporaryExtension(String parentId) {
        String[] parts = parentId.split("\\.");
        String prefix = parts[0] + "." + parts[1] + ".TE";
        int index = StateCounterTracker.nextTEIndex(parts[0].substring(0,2));
        return prefix + String.format("%02d", index);
    }
}
