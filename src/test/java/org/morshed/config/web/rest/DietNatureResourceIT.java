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
import org.morshed.config.domain.DietNature;
import org.morshed.config.repository.DietNatureRepository;
import org.morshed.web.rest.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DietNatureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DietNatureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/diet-natures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DietNatureRepository dietNatureRepository;

    @Autowired
    private WebTestClient webTestClient;

    private DietNature dietNature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DietNature createEntity() {
        DietNature dietNature = new DietNature().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return dietNature;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DietNature createUpdatedEntity() {
        DietNature dietNature = new DietNature().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return dietNature;
    }

    @BeforeEach
    public void initTest() {
        dietNatureRepository.deleteAll().block();
        dietNature = createEntity();
    }

    @Test
    void createDietNature() throws Exception {
        int databaseSizeBeforeCreate = dietNatureRepository.findAll().collectList().block().size();
        // Create the DietNature
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeCreate + 1);
        DietNature testDietNature = dietNatureList.get(dietNatureList.size() - 1);
        assertThat(testDietNature.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDietNature.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createDietNatureWithExistingId() throws Exception {
        // Create the DietNature with an existing ID
        dietNature.setId("existing_id");

        int databaseSizeBeforeCreate = dietNatureRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDietNatures() {
        // Initialize the database
        dietNatureRepository.save(dietNature).block();

        // Get all the dietNatureList
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
            .value(hasItem(dietNature.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getDietNature() {
        // Initialize the database
        dietNatureRepository.save(dietNature).block();

        // Get the dietNature
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dietNature.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dietNature.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getNonExistingDietNature() {
        // Get the dietNature
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDietNature() throws Exception {
        // Initialize the database
        dietNatureRepository.save(dietNature).block();

        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();

        // Update the dietNature
        DietNature updatedDietNature = dietNatureRepository.findById(dietNature.getId()).block();
        updatedDietNature.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDietNature.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDietNature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
        DietNature testDietNature = dietNatureList.get(dietNatureList.size() - 1);
        assertThat(testDietNature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDietNature.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingDietNature() throws Exception {
        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();
        dietNature.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dietNature.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDietNature() throws Exception {
        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();
        dietNature.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDietNature() throws Exception {
        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();
        dietNature.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDietNatureWithPatch() throws Exception {
        // Initialize the database
        dietNatureRepository.save(dietNature).block();

        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();

        // Update the dietNature using partial update
        DietNature partialUpdatedDietNature = new DietNature();
        partialUpdatedDietNature.setId(dietNature.getId());

        partialUpdatedDietNature.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDietNature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDietNature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
        DietNature testDietNature = dietNatureList.get(dietNatureList.size() - 1);
        assertThat(testDietNature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDietNature.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateDietNatureWithPatch() throws Exception {
        // Initialize the database
        dietNatureRepository.save(dietNature).block();

        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();

        // Update the dietNature using partial update
        DietNature partialUpdatedDietNature = new DietNature();
        partialUpdatedDietNature.setId(dietNature.getId());

        partialUpdatedDietNature.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDietNature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDietNature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
        DietNature testDietNature = dietNatureList.get(dietNatureList.size() - 1);
        assertThat(testDietNature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDietNature.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingDietNature() throws Exception {
        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();
        dietNature.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dietNature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDietNature() throws Exception {
        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();
        dietNature.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDietNature() throws Exception {
        int databaseSizeBeforeUpdate = dietNatureRepository.findAll().collectList().block().size();
        dietNature.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dietNature))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DietNature in the database
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDietNature() {
        // Initialize the database
        dietNatureRepository.save(dietNature).block();

        int databaseSizeBeforeDelete = dietNatureRepository.findAll().collectList().block().size();

        // Delete the dietNature
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dietNature.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DietNature> dietNatureList = dietNatureRepository.findAll().collectList().block();
        assertThat(dietNatureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
