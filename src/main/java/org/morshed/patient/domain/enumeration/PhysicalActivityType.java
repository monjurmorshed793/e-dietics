package org.morshed.patient.domain.enumeration;

/**
 * The PhysicalActivityType enumeration.
 */
public enum PhysicalActivityType {
    REGULAR("Regular"),
    SEASONAL("Seasonal");

    private final String value;

    PhysicalActivityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
