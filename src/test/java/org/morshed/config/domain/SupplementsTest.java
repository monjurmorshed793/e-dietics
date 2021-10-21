package org.morshed.config.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class SupplementsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplements.class);
        Supplements supplements1 = new Supplements();
        supplements1.setId("id1");
        Supplements supplements2 = new Supplements();
        supplements2.setId(supplements1.getId());
        assertThat(supplements1).isEqualTo(supplements2);
        supplements2.setId("id2");
        assertThat(supplements1).isNotEqualTo(supplements2);
        supplements1.setId(null);
        assertThat(supplements1).isNotEqualTo(supplements2);
    }
}
