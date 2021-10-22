package org.morshed.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class ComponentNavigationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponentNavigation.class);
        ComponentNavigation componentNavigation1 = new ComponentNavigation();
        componentNavigation1.setId("id1");
        ComponentNavigation componentNavigation2 = new ComponentNavigation();
        componentNavigation2.setId(componentNavigation1.getId());
        assertThat(componentNavigation1).isEqualTo(componentNavigation2);
        componentNavigation2.setId("id2");
        assertThat(componentNavigation1).isNotEqualTo(componentNavigation2);
        componentNavigation1.setId(null);
        assertThat(componentNavigation1).isNotEqualTo(componentNavigation2);
    }
}
