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
import org.morshed.domain.MeasurementType;
import org.morshed.repository.MeasurementTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link MeasurementTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class MeasurementTypeResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/measurement-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;

    @Autowired
    private WebTestClient webTestClient;

    private MeasurementType measurementType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasurementType createEntity() {
        MeasurementType measurementType = new MeasurementType().label(DEFAULT_LABEL).description(DEFAULT_DESCRIPTION);
        return measurementType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasurementType createUpdatedEntity() {
        MeasurementType measurementType = new MeasurementType().label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);
        return measurementType;
    }

    @BeforeEach
    public void initTest() {
        measurementTypeRepository.deleteAll().block();
        measurementType = createEntity();
    }

    @Test
    void createMeasurementType() throws Exception {
        int databaseSizeBeforeCreate = measurementTypeRepository.findAll().collectList().block().size();
        // Create the MeasurementType
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testMeasurementType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createMeasurementTypeWithExistingId() throws Exception {
        // Create the MeasurementType with an existing ID
        measurementType.setId("existing_id");

        int databaseSizeBeforeCreate = measurementTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementTypeRepository.findAll().collectList().block().size();
        // set the field null
        measurementType.setLabel(null);

        // Create the MeasurementType, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMeasurementTypes() {
        // Initialize the database
        measurementTypeRepository.save(measurementType).block();

        // Get all the measurementTypeList
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
            .value(hasItem(measurementType.getId()))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getMeasurementType() {
        // Initialize the database
        measurementTypeRepository.save(measurementType).block();

        // Get the measurementType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, measurementType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(measurementType.getId()))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingMeasurementType() {
        // Get the measurementType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewMeasurementType() throws Exception {
        // Initialize the database
        measurementTypeRepository.save(measurementType).block();

        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();

        // Update the measurementType
        MeasurementType updatedMeasurementType = measurementTypeRepository.findById(measurementType.getId()).block();
        updatedMeasurementType.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMeasurementType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMeasurementType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testMeasurementType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();
        measurementType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, measurementType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();
        measurementType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();
        measurementType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMeasurementTypeWithPatch() throws Exception {
        // Initialize the database
        measurementTypeRepository.save(measurementType).block();

        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();

        // Update the measurementType using partial update
        MeasurementType partialUpdatedMeasurementType = new MeasurementType();
        partialUpdatedMeasurementType.setId(measurementType.getId());

        partialUpdatedMeasurementType.label(UPDATED_LABEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMeasurementType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasurementType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testMeasurementType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateMeasurementTypeWithPatch() throws Exception {
        // Initialize the database
        measurementTypeRepository.save(measurementType).block();

        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();

        // Update the measurementType using partial update
        MeasurementType partialUpdatedMeasurementType = new MeasurementType();
        partialUpdatedMeasurementType.setId(measurementType.getId());

        partialUpdatedMeasurementType.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMeasurementType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasurementType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasurementType testMeasurementType = measurementTypeList.get(measurementTypeList.size() - 1);
        assertThat(testMeasurementType.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testMeasurementType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();
        measurementType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, measurementType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();
        measurementType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMeasurementType() throws Exception {
        int databaseSizeBeforeUpdate = measurementTypeRepository.findAll().collectList().block().size();
        measurementType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(measurementType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MeasurementType in the database
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMeasurementType() {
        // Initialize the database
        measurementTypeRepository.save(measurementType).block();

        int databaseSizeBeforeDelete = measurementTypeRepository.findAll().collectList().block().size();

        // Delete the measurementType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, measurementType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findAll().collectList().block();
        assertThat(measurementTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
