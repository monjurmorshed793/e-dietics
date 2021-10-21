package org.morshed.patient.domain.enumeration;

/**
 * The WeightType enumeration.
 */
public enum WeightType {
    UNDERWEIGHT("Underweight"),
    NORMAL("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese");

    private final String value;

    WeightType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
