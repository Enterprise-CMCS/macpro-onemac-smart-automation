package gov.cms.smart.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class IdentifyingInfo {
    private String idNumber;
    private String state;
    private String authority;

}
