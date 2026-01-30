package gov.cms.smart.models.enums;

import lombok.Getter;


@Getter
public enum PriorityCode {

    ESCALATED_REVIEW("P1 - Escalated Review"),
    CUSTOMARY_REVIEW("P2 - Customary/Routine Review"),
    EXPEDITED_REVIEW("P3 - Expedited Review");

    private final String value;


    PriorityCode(String value) {
        this.value = value;
    }
    public static PriorityCode fromUiValue(String text) {
        for (PriorityCode code : values()) {
            if (code.value.equalsIgnoreCase(text.trim())) {
                return code;
            }
        }
        throw new IllegalArgumentException("Unknown UI Priority Code: " + text);
    }
}

