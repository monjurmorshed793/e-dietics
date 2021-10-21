package org.morshed.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.morshed.web.rest.TestUtil;

class ActivityLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityLevel.class);
        ActivityLevel activityLevel1 = new ActivityLevel();
        activityLevel1.setId("id1");
        ActivityLevel activityLevel2 = new ActivityLevel();
        activityLevel2.setId(activityLevel1.getId());
        assertThat(activityLevel1).isEqualTo(activityLevel2);
        activityLevel2.setId("id2");
        assertThat(activityLevel1).isNotEqualTo(activityLevel2);
        activityLevel1.setId(null);
        assertThat(activityLevel1).isNotEqualTo(activityLevel2);
    }
}
