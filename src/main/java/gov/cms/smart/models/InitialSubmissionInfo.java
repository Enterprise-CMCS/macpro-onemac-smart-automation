package gov.cms.smart.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InitialSubmissionInfo {

    private String fifteenthDay;
    private String twentyFifthDay;
    private String sixtiethDay;
    private String seventyFifthDay;
    private String ninetiethDayEndDate;

    private String initialSubmissionDate;
    private String secondClockStartDate;

    private String approvedEffectiveDate;
    private String proposedEffectiveDate;

    private String companionLetterRequestedDate;
    private String companionLetterReceivedDate;
    private String companionLetterDueDate;

}
