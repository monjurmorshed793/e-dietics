package org.morshed.domain.enumeration;

/**
 * The GainLossType enumeration.
 */
public enum GainLossType {
    INTENTIONAL("Intentional"),
    UNINTENTIONAL("Unintentional");

    private final String value;

    GainLossType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
