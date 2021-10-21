package org.morshed.patient.domain.enumeration;

/**
 * The HeightMeasureType enumeration.
 */
public enum HeightMeasureType {
    CM("cm"),
    INCH("inch"),
    FEET("ft");

    private final String value;

    HeightMeasureType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
