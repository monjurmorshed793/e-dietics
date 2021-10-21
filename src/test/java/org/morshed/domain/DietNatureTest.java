package org.morshed.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class DietNatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DietNature.class);
        DietNature dietNature1 = new DietNature();
        dietNature1.setId("id1");
        DietNature dietNature2 = new DietNature();
        dietNature2.setId(dietNature1.getId());
        assertThat(dietNature1).isEqualTo(dietNature2);
        dietNature2.setId("id2");
        assertThat(dietNature1).isNotEqualTo(dietNature2);
        dietNature1.setId(null);
        assertThat(dietNature1).isNotEqualTo(dietNature2);
    }
}
