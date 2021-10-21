package org.morshed.patient.domain.enumeration;

/**
 * The ReligionType enumeration.
 */
public enum ReligionType {
    MUSLIM("Muslim"),
    HINDU("Hindu"),
    BUDDHIST("Buddhist"),
    CHRISTIANITY("Christianity"),
    JEWS("Jews"),
    OTHERS("Others");

    private final String value;

    ReligionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
