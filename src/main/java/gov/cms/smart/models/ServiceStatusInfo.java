package gov.cms.smart.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class ServiceStatusInfo {

    private String initialSubmissionComplete;   // Yes/No
    private String missingInformation;          // Text or Yes/No depending on UI

    private String type;                        // e.g. Eligibility
    private String subtype;                     // e.g. Eligibility Process
    private String documentsPostedTo;           // Dropdown

    private String spaBlockedByPendingWaivers;  // Yes/No
    private String blockingSpasMemo;            // Text

    private String fifteenthDayCallHeld;        // Yes/No
    private String reasonNoFifteenthDayCall;    // Text


}
