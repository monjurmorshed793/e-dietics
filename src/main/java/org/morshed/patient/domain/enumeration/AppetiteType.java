package org.morshed.patient.domain.enumeration;

/**
 * The AppetiteType enumeration.
 */
public enum AppetiteType {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor");

    private final String value;

    AppetiteType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
