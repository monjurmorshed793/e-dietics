package org.morshed.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.morshed.IntegrationTest;
import org.morshed.domain.NutritionState;
import org.morshed.repository.NutritionStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link NutritionStateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class NutritionStateResourceIT {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nutrition-states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private NutritionStateRepository nutritionStateRepository;

    @Autowired
    private WebTestClient webTestClient;

    private NutritionState nutritionState;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NutritionState createEntity() {
        NutritionState nutritionState = new NutritionState().order(DEFAULT_ORDER).label(DEFAULT_LABEL).note(DEFAULT_NOTE);
        return nutritionState;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NutritionState createUpdatedEntity() {
        NutritionState nutritionState = new NutritionState().order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);
        return nutritionState;
    }

    @BeforeEach
    public void initTest() {
        nutritionStateRepository.deleteAll().block();
        nutritionState = createEntity();
    }

    @Test
    void createNutritionState() throws Exception {
        int databaseSizeBeforeCreate = nutritionStateRepository.findAll().collectList().block().size();
        // Create the NutritionState
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeCreate + 1);
        NutritionState testNutritionState = nutritionStateList.get(nutritionStateList.size() - 1);
        assertThat(testNutritionState.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testNutritionState.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testNutritionState.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    void createNutritionStateWithExistingId() throws Exception {
        // Create the NutritionState with an existing ID
        nutritionState.setId("existing_id");

        int databaseSizeBeforeCreate = nutritionStateRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = nutritionStateRepository.findAll().collectList().block().size();
        // set the field null
        nutritionState.setLabel(null);

        // Create the NutritionState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNutritionStates() {
        // Initialize the database
        nutritionStateRepository.save(nutritionState).block();

        // Get all the nutritionStateList
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
            .value(hasItem(nutritionState.getId()))
            .jsonPath("$.[*].order")
            .value(hasItem(DEFAULT_ORDER))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL))
            .jsonPath("$.[*].note")
            .value(hasItem(DEFAULT_NOTE.toString()));
    }

    @Test
    void getNutritionState() {
        // Initialize the database
        nutritionStateRepository.save(nutritionState).block();

        // Get the nutritionState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, nutritionState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(nutritionState.getId()))
            .jsonPath("$.order")
            .value(is(DEFAULT_ORDER))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL))
            .jsonPath("$.note")
            .value(is(DEFAULT_NOTE.toString()));
    }

    @Test
    void getNonExistingNutritionState() {
        // Get the nutritionState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewNutritionState() throws Exception {
        // Initialize the database
        nutritionStateRepository.save(nutritionState).block();

        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();

        // Update the nutritionState
        NutritionState updatedNutritionState = nutritionStateRepository.findById(nutritionState.getId()).block();
        updatedNutritionState.order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedNutritionState.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedNutritionState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
        NutritionState testNutritionState = nutritionStateList.get(nutritionStateList.size() - 1);
        assertThat(testNutritionState.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testNutritionState.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testNutritionState.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void putNonExistingNutritionState() throws Exception {
        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();
        nutritionState.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, nutritionState.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNutritionState() throws Exception {
        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();
        nutritionState.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNutritionState() throws Exception {
        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();
        nutritionState.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNutritionStateWithPatch() throws Exception {
        // Initialize the database
        nutritionStateRepository.save(nutritionState).block();

        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();

        // Update the nutritionState using partial update
        NutritionState partialUpdatedNutritionState = new NutritionState();
        partialUpdatedNutritionState.setId(nutritionState.getId());

        partialUpdatedNutritionState.label(UPDATED_LABEL).note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNutritionState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNutritionState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
        NutritionState testNutritionState = nutritionStateList.get(nutritionStateList.size() - 1);
        assertThat(testNutritionState.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testNutritionState.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testNutritionState.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void fullUpdateNutritionStateWithPatch() throws Exception {
        // Initialize the database
        nutritionStateRepository.save(nutritionState).block();

        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();

        // Update the nutritionState using partial update
        NutritionState partialUpdatedNutritionState = new NutritionState();
        partialUpdatedNutritionState.setId(nutritionState.getId());

        partialUpdatedNutritionState.order(UPDATED_ORDER).label(UPDATED_LABEL).note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNutritionState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNutritionState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
        NutritionState testNutritionState = nutritionStateList.get(nutritionStateList.size() - 1);
        assertThat(testNutritionState.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testNutritionState.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testNutritionState.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void patchNonExistingNutritionState() throws Exception {
        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();
        nutritionState.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, nutritionState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNutritionState() throws Exception {
        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();
        nutritionState.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNutritionState() throws Exception {
        int databaseSizeBeforeUpdate = nutritionStateRepository.findAll().collectList().block().size();
        nutritionState.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nutritionState))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NutritionState in the database
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNutritionState() {
        // Initialize the database
        nutritionStateRepository.save(nutritionState).block();

        int databaseSizeBeforeDelete = nutritionStateRepository.findAll().collectList().block().size();

        // Delete the nutritionState
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, nutritionState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<NutritionState> nutritionStateList = nutritionStateRepository.findAll().collectList().block();
        assertThat(nutritionStateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
