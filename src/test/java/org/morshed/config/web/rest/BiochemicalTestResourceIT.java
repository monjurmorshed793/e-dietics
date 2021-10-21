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
import org.morshed.config.domain.BiochemicalTest;
import org.morshed.config.repository.BiochemicalTestRepository;
import org.morshed.web.rest.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link BiochemicalTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class BiochemicalTestResourceIT {

    private static final String DEFAULT_TEST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/biochemical-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BiochemicalTestRepository biochemicalTestRepository;

    @Autowired
    private WebTestClient webTestClient;

    private BiochemicalTest biochemicalTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BiochemicalTest createEntity() {
        BiochemicalTest biochemicalTest = new BiochemicalTest().testName(DEFAULT_TEST_NAME).description(DEFAULT_DESCRIPTION);
        return biochemicalTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BiochemicalTest createUpdatedEntity() {
        BiochemicalTest biochemicalTest = new BiochemicalTest().testName(UPDATED_TEST_NAME).description(UPDATED_DESCRIPTION);
        return biochemicalTest;
    }

    @BeforeEach
    public void initTest() {
        biochemicalTestRepository.deleteAll().block();
        biochemicalTest = createEntity();
    }

    @Test
    void createBiochemicalTest() throws Exception {
        int databaseSizeBeforeCreate = biochemicalTestRepository.findAll().collectList().block().size();
        // Create the BiochemicalTest
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeCreate + 1);
        BiochemicalTest testBiochemicalTest = biochemicalTestList.get(biochemicalTestList.size() - 1);
        assertThat(testBiochemicalTest.getTestName()).isEqualTo(DEFAULT_TEST_NAME);
        assertThat(testBiochemicalTest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createBiochemicalTestWithExistingId() throws Exception {
        // Create the BiochemicalTest with an existing ID
        biochemicalTest.setId("existing_id");

        int databaseSizeBeforeCreate = biochemicalTestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBiochemicalTests() {
        // Initialize the database
        biochemicalTestRepository.save(biochemicalTest).block();

        // Get all the biochemicalTestList
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
            .value(hasItem(biochemicalTest.getId()))
            .jsonPath("$.[*].testName")
            .value(hasItem(DEFAULT_TEST_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getBiochemicalTest() {
        // Initialize the database
        biochemicalTestRepository.save(biochemicalTest).block();

        // Get the biochemicalTest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, biochemicalTest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(biochemicalTest.getId()))
            .jsonPath("$.testName")
            .value(is(DEFAULT_TEST_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getNonExistingBiochemicalTest() {
        // Get the biochemicalTest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBiochemicalTest() throws Exception {
        // Initialize the database
        biochemicalTestRepository.save(biochemicalTest).block();

        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();

        // Update the biochemicalTest
        BiochemicalTest updatedBiochemicalTest = biochemicalTestRepository.findById(biochemicalTest.getId()).block();
        updatedBiochemicalTest.testName(UPDATED_TEST_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBiochemicalTest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBiochemicalTest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
        BiochemicalTest testBiochemicalTest = biochemicalTestList.get(biochemicalTestList.size() - 1);
        assertThat(testBiochemicalTest.getTestName()).isEqualTo(UPDATED_TEST_NAME);
        assertThat(testBiochemicalTest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();
        biochemicalTest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, biochemicalTest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();
        biochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();
        biochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBiochemicalTestWithPatch() throws Exception {
        // Initialize the database
        biochemicalTestRepository.save(biochemicalTest).block();

        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();

        // Update the biochemicalTest using partial update
        BiochemicalTest partialUpdatedBiochemicalTest = new BiochemicalTest();
        partialUpdatedBiochemicalTest.setId(biochemicalTest.getId());

        partialUpdatedBiochemicalTest.testName(UPDATED_TEST_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBiochemicalTest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBiochemicalTest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
        BiochemicalTest testBiochemicalTest = biochemicalTestList.get(biochemicalTestList.size() - 1);
        assertThat(testBiochemicalTest.getTestName()).isEqualTo(UPDATED_TEST_NAME);
        assertThat(testBiochemicalTest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateBiochemicalTestWithPatch() throws Exception {
        // Initialize the database
        biochemicalTestRepository.save(biochemicalTest).block();

        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();

        // Update the biochemicalTest using partial update
        BiochemicalTest partialUpdatedBiochemicalTest = new BiochemicalTest();
        partialUpdatedBiochemicalTest.setId(biochemicalTest.getId());

        partialUpdatedBiochemicalTest.testName(UPDATED_TEST_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBiochemicalTest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBiochemicalTest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
        BiochemicalTest testBiochemicalTest = biochemicalTestList.get(biochemicalTestList.size() - 1);
        assertThat(testBiochemicalTest.getTestName()).isEqualTo(UPDATED_TEST_NAME);
        assertThat(testBiochemicalTest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();
        biochemicalTest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, biochemicalTest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();
        biochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBiochemicalTest() throws Exception {
        int databaseSizeBeforeUpdate = biochemicalTestRepository.findAll().collectList().block().size();
        biochemicalTest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(biochemicalTest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BiochemicalTest in the database
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBiochemicalTest() {
        // Initialize the database
        biochemicalTestRepository.save(biochemicalTest).block();

        int databaseSizeBeforeDelete = biochemicalTestRepository.findAll().collectList().block().size();

        // Delete the biochemicalTest
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, biochemicalTest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BiochemicalTest> biochemicalTestList = biochemicalTestRepository.findAll().collectList().block();
        assertThat(biochemicalTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
