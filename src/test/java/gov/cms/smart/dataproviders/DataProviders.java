package gov.cms.smart.dataproviders;

import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Map;

public class DataProviders {

    public static final Map<String, List<String>> GROUP_TO_DIVISIONS = Map.of(
            "CAHPG", List.of("--None--", "DEPO", "DMEP", "DQHO", "DSCP", "DTA", "CAHPG - Office of Group Director"),
            "DSG", List.of("--None--", "DBDA", "DBE", "DHM", "DIS", "DSS", "DSG - Office of Group Director"),
            "FMG", List.of("--None--", "DFOE", "DFOW", "DFP", "DRR", "DRP", "FMG - Office of Group Director"),
            "MBHPG", List.of("--None--", "DBC", "DCST", "DHPC", "DLTSS", "DOP", "MBHPG - Office of Group Director"),
            "MCG", List.of("--None--", "DMCO", "DMCP", "MCG - Office of Group Director"),
            "MCOG", List.of("--None--", "DHOO", "DPO", "MCOG - Office of Group Director"),
            "OSG", List.of("--None--", "DBA", "DCO", "DHC", "DOES", "OSG - Office of Group Director"),
            "SDG", List.of("--None--", "DDME", "DECD", "DSRD", "SDG - Office of Group Director")
    );

    @DataProvider(name = "groupDivisionData")
    public static Object[][] groupDivisionData() {
        Object[][] data = new Object[GROUP_TO_DIVISIONS.size()][1];
        int i = 0;
        for (String group : GROUP_TO_DIVISIONS.keySet()) {
            data[i][0] = group;
            i++;
        }
        return data;
    }

    public static final Map<String, List<String>> STATUS_TO_SUBSTATUS = Map.of(
            "Closed", List.of("--None--", "Approved", "Disapproved", "Withdrawn"),
            "First Clock - Under Review", List.of("--None--", "Intake Needed", "Pending Concurrence", "Pending Approval", "Pending Clearance"),
            "Second Clock - Under Review", List.of("--None--", "Pending Concurrence", "Pending Approval", "Pending Clearance"),
            "RAI", List.of("--None--", "RAI Issued", "RAI Withdraw Requested")
    );

    @DataProvider(name = "statusSubstatusData")
    public static Object[][] statusSubstatusData() {
        Object[][] data = new Object[STATUS_TO_SUBSTATUS.size()][1];
        int i = 0;
        for (String group : STATUS_TO_SUBSTATUS.keySet()) {
            data[i][0] = group;
            i++;
        }
        return data;
    }

    public static final Map<String, List<String>> TYPE_TO_SUBTYPE = Map.of(
            "Eligibility", List.of("--None--", "Eligibility Process", "MAGI Eligibility & Methods", "Citizenship Documentation", "Medicaid Expansion", "Nonfinancial Eligibility", "Premiums", "Other"),
            "Benefits", List.of("--None--", "Benchmark", "Change Benefit Limit", "New Benefit", "Provider Qualifications", "Other"),
            "Payment", List.of("--None--", "Rate Change", "Process Change", "Other")
    );

    @DataProvider(name = "typeSubtypeData")
    public static Object[][] typeSubtypeData() {
        Object[][] data = new Object[TYPE_TO_SUBTYPE.size()][1];
        int i = 0;
        for (String group : TYPE_TO_SUBTYPE.keySet()) {
            data[i][0] = group;
            i++;
        }
        return data;
    }

    @DataProvider(name = "priorityLevels")
    public Object[][] priorityLevels() {
        return new Object[][]{
                {PriorityCode.ESCALATED_REVIEW, CodingAssessment.SAME},
                {PriorityCode.CUSTOMARY_REVIEW, CodingAssessment.UP_CODED},
                {PriorityCode.EXPEDITED_REVIEW, CodingAssessment.DOWN_CODED}
        };
    }

}