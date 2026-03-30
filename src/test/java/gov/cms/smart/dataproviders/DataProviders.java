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

    public static final Map<String, List<String>> TYPE_TO_SUBTYPE = Map.ofEntries(

            Map.entry("Eligibility", List.of(
                    "Aged/Blind/Disabled Eligibility",
                    "Application",
                    "Asset Verification System",
                    "Authorized Representative",
                    "Breast & Cervical Cancer",
                    "Citizenship and Non-Citizen Eligibility",
                    "Cost of Living Adjustment",
                    "Cost Sharing/Copayment",
                    "Disabled Children",
                    "Disregards",
                    "Eligibility Process",
                    "Express Lane",
                    "Family Planning",
                    "Family/Adult Eligibility",
                    "Federal Benefit Rate",
                    "Home Equity",
                    "Hospital Presumptive Eligibility",
                    "Income Standard–Territories",
                    "Income/Resource Methodologies",
                    "Institutional Eligibility",
                    "Medically Needy",
                    "Medicare Savings Programs (Duals)",
                    "Post Eligibility Treatment of Income",
                    "Pregnant Women",
                    "Presumptive Eligibility",
                    "Public Assistance Reporting Information System",
                    "Residency",
                    "Spenddown",
                    "Spousal Impoverishment",
                    "Transfer of Assets",
                    "Treatment of Trusts",
                    "ARP 9817",
                    "Extended Postpartum",
                    "PHE Related"
            )),

            Map.entry("Recovery Audit Contractor", List.of(
                    "RAC", "ARP 9817", "PHE Related"
            )),

            Map.entry("Disaster Relief", List.of(
                    "COVID", "Flood", "Hurricane", "Other Pandemic",
                    "Tropical Storms", "Viral Outbreak", "Wild Fire",
                    "Other", "ARP 9817", "PHE Related",
                    "Unwinding", "Cyberattack"
            )),

            Map.entry("Administrative", List.of(
                    "Authority Change", "Electronic Claims",
                    "Fair Hearings and Appeals", "Policy Update",
                    "Provider Appeal Timeframe", "Provider sanctions",
                    "Remove Obsolete Pages", "Remove Svc Category",
                    "Single State Agency", "State Review Designation",
                    "Table of Contents Update", "Utilization review Administration",
                    "ARP 9817", "PHE Related"
            )),

            Map.entry("Estate Recovery", List.of(
                    "Policy", "ARP 9817", "PHE Related"
            )),
            Map.entry("Reimbursement – Non-Institutional", List.of(
                    "1931 Parents/Caretaker",
                    "ABI",
                    "Adult Foster Care",
                    "Aged-blind-disabled",
                    "Ambulatory Surgery Svcs",
                    "Benchmark",
                    "Bundled Payments",
                    "Case Management",
                    "Child Behavioral Services",
                    "Child Foster Care",
                    "Children non Foster Care",
                    "CHIP Authority exp",
                    "Chiropractor",
                    "Chron Disease Prev Incent",
                    "Clinic",
                    "Community Mental Health Centers",
                    "Coverage Services SPA",
                    "Dental",
                    "Disease Management",
                    "Disproportionate Share Hospital",
                    "DSH Reduction Methodology",
                    "Early and Periodic Screening, Diagnostic, and Treatment",
                    "End Stage Renal Disease",
                    "Episodes of Care",
                    "EPSDT non School Based Services",
                    "Eyeglasses",
                    "Family Planning",
                    "Federally Qualified Health Centers",
                    "Federally Qualified Health Centers/ Rural Health Centers",
                    "Fee Schedule",
                    "Freestanding Birth Centers",
                    "Health Care Reform",
                    "Health Homes",
                    "Health Homes Chronic Cond",
                    "Home Health Services",
                    "Hospice",
                    "Hospice for Children",
                    "IHS Encounter Payment",
                    "Integrated Care Model",
                    "Lead Paint Inspec/Remed",
                    "Licensed Providers",
                    "Maternity",
                    "Medicaid Treatment Child Care",
                    "Medical Day Care",
                    "Medically Fragile Adult",
                    "Medically Fragile Child",
                    "Mental Health",
                    "Mentally Ill",
                    "Newborn/New Mothers",
                    "Nurse Anesthetist",
                    "Nutritionist",
                    "Occupational Therapy",
                    "OP",
                    "OP Hospital",
                    "Optometrist",
                    "Other",
                    "Patient Centered Medical Home",
                    "Pediatric Vaccine Admin",
                    "Personal Care",
                    "Personal Care Assistants",
                    "Persons with MR or DD",
                    "Physical Therapy",
                    "Physician",
                    "Physician Administered Drugs",
                    "Physician Services",
                    "Podiatrist",
                    "Preg Wom Tobbacco Cess 21",
                    "Pregnant Women",
                    "Preventable Conditions",
                    "Preventive",
                    "Primary Care Services Pmt",
                    "Private Duty Nursing",
                    "Program of All-Inclusive Care for the Elderly",
                    "Psychologist Services",
                    "Rate Modification",
                    "Rehab Services non School Based Service",
                    "Rehabilitative",
                    "Reimbursement Method",
                    "SBS EPSDT/Rehab Prov",
                    "School Based Services (SBS)",
                    "SPA Companion Letter NIPT",
                    "Speech Therapy",
                    "Substance Abuse",
                    "Supplemental Payments",
                    "Targeted Case Management",
                    "Telemedicine",
                    "TRAN",
                    "Transportation",
                    "Traumatic Brain Injury",
                    "Tuberculosis",
                    "ARP 9817",
                    "PHE Related",
                    "CCBHC",
                    "SBS – Free Care",
                    "TCM in Carceral Settings"
            )),

            Map.entry("Reimbursement – FMAP", List.of(
                    "Federal Medical Assistance Percentage",
                    "ARP 9817",
                    "PHE Related"
            )),

            Map.entry("Reimbursement – UPL", List.of(
                    "Upper Payment Limits",
                    "ARP 9817",
                    "PHE Related"
            )), Map.entry("Reimbursement – Institutional", List.of(
                    "Behavioral Health Providers",
                    "Diagnosis Related Group Methodology",
                    "Disproportionate Share Hospital",
                    "Hospital Acquired Conditions",
                    "Hospital DSH",
                    "Hospital Inpatient",
                    "Hospital non DSH",
                    "Intermediate Care Facility for Individuals with Intellectual Disabilities",
                    "Nursing Facility",
                    "Nursing facility-PASRR",
                    "Other",
                    "Outpatient Hospital",
                    "Preventive Conditions",
                    "Provider Tax",
                    "Psych Res Treat Facility",
                    "Upper Payment Limit Demonstrations",
                    "ARP 9817",
                    "PHE Related"
            )),

            Map.entry("Reimbursement – Adm", List.of(
                    "Administration",
                    "ARP 9817",
                    "PHE Related"
            )), Map.entry("Coverage – Non-Institutional 1905(a) Services", List.of(
                    "Alternative Benefit Plan",
                    "Amb Srvcs-Pregnant Women",
                    "Behavioral Services",
                    "Case Management",
                    "Chiropractic Services",
                    "Clinic Services",
                    "Dental",
                    "Denture",
                    "Disease Management",
                    "Drugs & Related Services",
                    "Emergency Services for Aliens",
                    "EPSDT",
                    "Ext Svcs-Pregnant Women",
                    "Eyeglasses",
                    "Family Planning",
                    "Federally Qualified Health Centers",
                    "Freestanding Birth Centers",
                    "Home Health",
                    "Hospice",
                    "Inpatient Hospital",
                    "Integrated Care Model",
                    "Lab and X-ray Services",
                    "Lactation Consultation",
                    "Managed Care",
                    "Mandatory MAT Services (1006b)",
                    "Medical & Surgical",
                    "Multiple Services",
                    "not IMD Inpatient Hosp",
                    "Certified Nurse Midwife",
                    "Certified Pediatric/Family Nurse Practitioner",
                    "Nursing Home Stays",
                    "Optometrist Services",
                    "Organ Transplant",
                    "Other Licensed Practitioners",
                    "Outpatient Hospital",
                    "Personal Care Services",
                    "Physician Services",
                    "Podiatrist Services",
                    "Preventative Services",
                    "Primary Care Case Management",
                    "Private Duty Nursing",
                    "Prosthetics",
                    "Rehab Services",
                    "Respiratory Care",
                    "Rural Health Clinics",
                    "School Based Services",
                    "Screening",
                    "Sickle Cell Treatment",
                    "TB Related Services",
                    "TCM-Targeted Case Management",
                    "Telemedicine",
                    "Therapies – PT/OT/Speech & Hearing",
                    "Tobacco Cessation",
                    "Transportation",
                    "ARP 9817",
                    "PHE Related",
                    "Adult ACIP Vaccines",
                    "CAA 5121",
                    "CAA 5121TCM",
                    "CAA 5122"))

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

        PriorityCode[] priorityCodes = PriorityCode.values();
        CodingAssessment[] codingAssessments = CodingAssessment.values();

        Object[][] data = new Object[priorityCodes.length * codingAssessments.length][2];

        int index = 0;

        for (PriorityCode priority : priorityCodes) {
            for (CodingAssessment assessment : codingAssessments) {
                data[index++] = new Object[]{priority, assessment};
            }
        }

        return data;
    }

}