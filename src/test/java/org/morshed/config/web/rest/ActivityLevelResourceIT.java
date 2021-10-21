package org.morshed.config.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.morshed.IntegrationTest;
import org.morshed.config.domain.ActivityLevel;
import org.morshed.config.repository.ActivityLevelRepository;
import org.morshed.web.rest.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ActivityLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ActivityLevelResourceIT {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/activity-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ActivityLevelRepository activityLevelRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ActivityLevel activityLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityLevel createEntity() {
        ActivityLevel activityLevel = new ActivityLevel().order(DEFAULT_ORDER).label(DEFAULT_LABEL).note(DEFAULT_NOTE);
        return activityLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityLevel createUpdatedEntity() {
        ActivityLevel activityLevel = new ActivityLevel().order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);
        return activityLevel;
    }

    @BeforeEach
    public void initTest() {
        activityLevelRepository.deleteAll().block();
        activityLevel = createEntity();
    }

    @Test
    void createActivityLevel() throws Exception {
        int databaseSizeBeforeCreate = activityLevelRepository.findAll().collectList().block().size();
        // Create the ActivityLevel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityLevel testActivityLevel = activityLevelList.get(activityLevelList.size() - 1);
        assertThat(testActivityLevel.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testActivityLevel.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testActivityLevel.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    void createActivityLevelWithExistingId() throws Exception {
        // Create the ActivityLevel with an existing ID
        activityLevel.setId("existing_id");

        int databaseSizeBeforeCreate = activityLevelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityLevelRepository.findAll().collectList().block().size();
        // set the field null
        activityLevel.setLabel(null);

        // Create the ActivityLevel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllActivityLevels() {
        // Initialize the database
        activityLevelRepository.save(activityLevel).block();

        // Get all the activityLevelList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(activityLevel.getId()))
            .jsonPath("$.[*].order")
            .value(hasItem(DEFAULT_ORDER))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL))
            .jsonPath("$.[*].note")
            .value(hasItem(DEFAULT_NOTE.toString()));
    }

    @Test
    void getActivityLevel() {
        // Initialize the database
        activityLevelRepository.save(activityLevel).block();

        // Get the activityLevel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, activityLevel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(activityLevel.getId()))
            .jsonPath("$.order")
            .value(is(DEFAULT_ORDER))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL))
            .jsonPath("$.note")
            .value(is(DEFAULT_NOTE.toString()));
    }

    @Test
    void getNonExistingActivityLevel() {
        // Get the activityLevel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewActivityLevel() throws Exception {
        // Initialize the database
        activityLevelRepository.save(activityLevel).block();

        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();

        // Update the activityLevel
        ActivityLevel updatedActivityLevel = activityLevelRepository.findById(activityLevel.getId()).block();
        updatedActivityLevel.order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedActivityLevel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedActivityLevel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
        ActivityLevel testActivityLevel = activityLevelList.get(activityLevelList.size() - 1);
        assertThat(testActivityLevel.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testActivityLevel.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testActivityLevel.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void putNonExistingActivityLevel() throws Exception {
        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();
        activityLevel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, activityLevel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchActivityLevel() throws Exception {
        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();
        activityLevel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamActivityLevel() throws Exception {
        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();
        activityLevel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateActivityLevelWithPatch() throws Exception {
        // Initialize the database
        activityLevelRepository.save(activityLevel).block();

        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();

        // Update the activityLevel using partial update
        ActivityLevel partialUpdatedActivityLevel = new ActivityLevel();
        partialUpdatedActivityLevel.setId(activityLevel.getId());

        partialUpdatedActivityLevel.order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedActivityLevel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedActivityLevel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
        ActivityLevel testActivityLevel = activityLevelList.get(activityLevelList.size() - 1);
        assertThat(testActivityLevel.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testActivityLevel.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testActivityLevel.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void fullUpdateActivityLevelWithPatch() throws Exception {
        // Initialize the database
        activityLevelRepository.save(activityLevel).block();

        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();

        // Update the activityLevel using partial update
        ActivityLevel partialUpdatedActivityLevel = new ActivityLevel();
        partialUpdatedActivityLevel.setId(activityLevel.getId());

        partialUpdatedActivityLevel.order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedActivityLevel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedActivityLevel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
        ActivityLevel testActivityLevel = activityLevelList.get(activityLevelList.size() - 1);
        assertThat(testActivityLevel.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testActivityLevel.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testActivityLevel.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void patchNonExistingActivityLevel() throws Exception {
        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();
        activityLevel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, activityLevel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchActivityLevel() throws Exception {
        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();
        activityLevel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamActivityLevel() throws Exception {
        int databaseSizeBeforeUpdate = activityLevelRepository.findAll().collectList().block().size();
        activityLevel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityLevel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ActivityLevel in the database
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteActivityLevel() {
        // Initialize the database
        activityLevelRepository.save(activityLevel).block();

        int databaseSizeBeforeDelete = activityLevelRepository.findAll().collectList().block().size();

        // Delete the activityLevel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, activityLevel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ActivityLevel> activityLevelList = activityLevelRepository.findAll().collectList().block();
        assertThat(activityLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
