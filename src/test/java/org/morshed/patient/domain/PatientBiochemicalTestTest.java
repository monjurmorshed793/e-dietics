package org.morshed.patient.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class PatientBiochemicalTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientBiochemicalTest.class);
        PatientBiochemicalTest patientBiochemicalTest1 = new PatientBiochemicalTest();
        patientBiochemicalTest1.setId("id1");
        PatientBiochemicalTest patientBiochemicalTest2 = new PatientBiochemicalTest();
        patientBiochemicalTest2.setId(patientBiochemicalTest1.getId());
        assertThat(patientBiochemicalTest1).isEqualTo(patientBiochemicalTest2);
        patientBiochemicalTest2.setId("id2");
        assertThat(patientBiochemicalTest1).isNotEqualTo(patientBiochemicalTest2);
        patientBiochemicalTest1.setId(null);
        assertThat(patientBiochemicalTest1).isNotEqualTo(patientBiochemicalTest2);
    }
}
