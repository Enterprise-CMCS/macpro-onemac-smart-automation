package gov.cms.smart.models.enums;

import lombok.Getter;

@Getter
public enum CodingAssessment {

    UP_CODED("Up Coded"),
    DOWN_CODED("Down Coded"),
    SAME("Same");

    private final String value;

    CodingAssessment(String value) {
        this.value = value;
    }
    public static CodingAssessment fromUiValue(String text) {
        for (CodingAssessment code : values()) {
            if (code.value.equalsIgnoreCase(text.trim())) {
                return code;
            }
        }
        throw new IllegalArgumentException("Unknown UI Coding Assessment: " + text);
    }
}
