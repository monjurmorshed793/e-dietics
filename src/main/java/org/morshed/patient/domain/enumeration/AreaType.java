package org.morshed.patient.domain.enumeration;

/**
 * The AreaType enumeration.
 */
public enum AreaType {
    RURAL("Rural"),
    URBAN("Urban");

    private final String value;

    AreaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
