package org.morshed.patient.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.morshed.IntegrationTest;
import org.morshed.patient.domain.PatientBiochemicalTest;
import org.morshed.patient.repository.PatientBiochemicalTestRepository;
import org.morshed.web.rest.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PatientBiochemicalTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class PatientBiochemicalTestResourceIT {

    private static final String DEFAULT_OTHER = "AAAAAAAAAA";
    private static final String UPDATED_OTHER = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/patient-biochemical-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PatientBiochemicalTestRepository patientBiochemicalTestRepository;

    @Autowired
    private WebTestClient webTestClient;

    private PatientBiochemicalTest patientBiochemicalTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientBiochemicalTest createEntity() {
        PatientBiochemicalTest patientBiochemicalTest = new PatientBiochemicalTest().other(DEFAULT_OTHER).value(DEFAULT_VALUE);
        return patientBiochemicalTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientBiochemicalTest createUpdatedEntity() {
        PatientBiochemicalTest patientBiochemicalTest = new PatientBiochemicalTest().other(UPDATED_OTHER).value(UPDATED_VALUE);
        return patientBiochemicalTest;
    }

    @BeforeEach
    public void initTest() {
        patientBiochemicalTestRepository.deleteAll().block();
        patientBiochemicalTest = createEntity();
    }

    @Test
    void createPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeCreate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        // Create the PatientBiochemicalTest
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeCreate + 1);
        PatientBiochemicalTest testPatientBiochemicalTest = patientBiochemicalTestList.get(patientBiochemicalTestList.size() - 1);
        assertThat(testPatientBiochemicalTest.getOther()).isEqualTo(DEFAULT_OTHER);
        assertThat(testPatientBiochemicalTest.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createPatientBiochemicalTestWithExistingId() throws Exception {
        // Create the PatientBiochemicalTest with an existing ID
        patientBiochemicalTest.setId("existing_id");

        int databaseSizeBeforeCreate = patientBiochemicalTestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPatientBiochemicalTests() {
        // Initialize the database
        patientBiochemicalTestRepository.save(patientBiochemicalTest).block();

        // Get all the patientBiochemicalTestList
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
            .value(hasItem(patientBiochemicalTest.getId()))
            .jsonPath("$.[*].other")
            .value(hasItem(DEFAULT_OTHER))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    void getPatientBiochemicalTest() {
        // Initialize the database
        patientBiochemicalTestRepository.save(patientBiochemicalTest).block();

        // Get the patientBiochemicalTest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, patientBiochemicalTest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(patientBiochemicalTest.getId()))
            .jsonPath("$.other")
            .value(is(DEFAULT_OTHER))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    void getNonExistingPatientBiochemicalTest() {
        // Get the patientBiochemicalTest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPatientBiochemicalTest() throws Exception {
        // Initialize the database
        patientBiochemicalTestRepository.save(patientBiochemicalTest).block();

        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();

        // Update the patientBiochemicalTest
        PatientBiochemicalTest updatedPatientBiochemicalTest = patientBiochemicalTestRepository
            .findById(patientBiochemicalTest.getId())
            .block();
        updatedPatientBiochemicalTest.other(UPDATED_OTHER).value(UPDATED_VALUE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPatientBiochemicalTest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPatientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
        PatientBiochemicalTest testPatientBiochemicalTest = patientBiochemicalTestList.get(patientBiochemicalTestList.size() - 1);
        assertThat(testPatientBiochemicalTest.getOther()).isEqualTo(UPDATED_OTHER);
        assertThat(testPatientBiochemicalTest.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        patientBiochemicalTest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, patientBiochemicalTest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        patientBiochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        patientBiochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePatientBiochemicalTestWithPatch() throws Exception {
        // Initialize the database
        patientBiochemicalTestRepository.save(patientBiochemicalTest).block();

        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();

        // Update the patientBiochemicalTest using partial update
        PatientBiochemicalTest partialUpdatedPatientBiochemicalTest = new PatientBiochemicalTest();
        partialUpdatedPatientBiochemicalTest.setId(patientBiochemicalTest.getId());

        partialUpdatedPatientBiochemicalTest.value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPatientBiochemicalTest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
        PatientBiochemicalTest testPatientBiochemicalTest = patientBiochemicalTestList.get(patientBiochemicalTestList.size() - 1);
        assertThat(testPatientBiochemicalTest.getOther()).isEqualTo(DEFAULT_OTHER);
        assertThat(testPatientBiochemicalTest.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void fullUpdatePatientBiochemicalTestWithPatch() throws Exception {
        // Initialize the database
        patientBiochemicalTestRepository.save(patientBiochemicalTest).block();

        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();

        // Update the patientBiochemicalTest using partial update
        PatientBiochemicalTest partialUpdatedPatientBiochemicalTest = new PatientBiochemicalTest();
        partialUpdatedPatientBiochemicalTest.setId(patientBiochemicalTest.getId());

        partialUpdatedPatientBiochemicalTest.other(UPDATED_OTHER).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPatientBiochemicalTest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
        PatientBiochemicalTest testPatientBiochemicalTest = patientBiochemicalTestList.get(patientBiochemicalTestList.size() - 1);
        assertThat(testPatientBiochemicalTest.getOther()).isEqualTo(UPDATED_OTHER);
        assertThat(testPatientBiochemicalTest.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        patientBiochemicalTest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, patientBiochemicalTest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        patientBiochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPatientBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = patientBiochemicalTestRepository.findAll().collectList().block().size();
        patientBiochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(patientBiochemicalTest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PatientBiochemicalTest in the database
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePatientBiochemicalTest() {
        // Initialize the database
        patientBiochemicalTestRepository.save(patientBiochemicalTest).block();

        int databaseSizeBeforeDelete = patientBiochemicalTestRepository.findAll().collectList().block().size();

        // Delete the patientBiochemicalTest
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, patientBiochemicalTest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PatientBiochemicalTest> patientBiochemicalTestList = patientBiochemicalTestRepository.findAll().collectList().block();
        assertThat(patientBiochemicalTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
