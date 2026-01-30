package gov.cms.smart.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class PackageDetails {

    private IdentifyingInfo identifyingInfo;
    private PriorityInfo priorityInfo;
    private InitialSubmissionInfo initialSubmissionInfo;
    private ServiceStatusInfo serviceStatusInfo;
    private PlanInfo planInfo;

}
