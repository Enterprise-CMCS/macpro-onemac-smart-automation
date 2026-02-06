package gov.cms.smart.utils.data;

public class SpaIdGenerator {

    public static String nextSpa(String state, String fiscalYear) {
        int next = StateCounterTracker.nextSpaNumber(state);
        return String.format("%s-%s-%04d", state, fiscalYear, next);
    }
}
