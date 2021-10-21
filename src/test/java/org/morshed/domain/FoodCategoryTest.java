package org.morshed.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class FoodCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodCategory.class);
        FoodCategory foodCategory1 = new FoodCategory();
        foodCategory1.setId("id1");
        FoodCategory foodCategory2 = new FoodCategory();
        foodCategory2.setId(foodCategory1.getId());
        assertThat(foodCategory1).isEqualTo(foodCategory2);
        foodCategory2.setId("id2");
        assertThat(foodCategory1).isNotEqualTo(foodCategory2);
        foodCategory1.setId(null);
        assertThat(foodCategory1).isNotEqualTo(foodCategory2);
    }
}
