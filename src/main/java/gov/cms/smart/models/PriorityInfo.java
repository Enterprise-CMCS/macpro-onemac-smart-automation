package gov.cms.smart.models;

import lombok.Getter;
import lombok.Setter;

public class PriorityInfo {
    @Setter
    @Getter
    private String priorityCode;
    private String codingAfterInitialAssessment;

    private String priorityComments;
    private String dateOfCodingChange;
}
