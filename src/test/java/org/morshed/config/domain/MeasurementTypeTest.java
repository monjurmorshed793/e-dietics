package org.morshed.config.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class MeasurementTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasurementType.class);
        MeasurementType measurementType1 = new MeasurementType();
        measurementType1.setId("id1");
        MeasurementType measurementType2 = new MeasurementType();
        measurementType2.setId(measurementType1.getId());
        assertThat(measurementType1).isEqualTo(measurementType2);
        measurementType2.setId("id2");
        assertThat(measurementType1).isNotEqualTo(measurementType2);
        measurementType1.setId(null);
        assertThat(measurementType1).isNotEqualTo(measurementType2);
    }
}
