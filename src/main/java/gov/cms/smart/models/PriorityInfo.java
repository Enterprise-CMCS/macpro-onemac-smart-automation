package gov.cms.smart.models;

import gov.cms.smart.models.enums.CodingAssessment;
import gov.cms.smart.models.enums.PriorityCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class PriorityInfo {
    // private String priorityCode;
    private PriorityCode priorityCode;
    private CodingAssessment codingAssessment;
    private String priorityComments;
    private String dateOfCodingChange;


}
