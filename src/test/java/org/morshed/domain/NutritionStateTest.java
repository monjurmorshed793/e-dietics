package org.morshed.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class NutritionStateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NutritionState.class);
        NutritionState nutritionState1 = new NutritionState();
        nutritionState1.setId("id1");
        NutritionState nutritionState2 = new NutritionState();
        nutritionState2.setId(nutritionState1.getId());
        assertThat(nutritionState1).isEqualTo(nutritionState2);
        nutritionState2.setId("id2");
        assertThat(nutritionState1).isNotEqualTo(nutritionState2);
        nutritionState1.setId(null);
        assertThat(nutritionState1).isNotEqualTo(nutritionState2);
    }
}
