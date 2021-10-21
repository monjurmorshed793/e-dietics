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
import org.morshed.config.domain.Supplements;
import org.morshed.config.repository.SupplementsRepository;
import org.morshed.web.rest.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SupplementsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SupplementsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SupplementsRepository supplementsRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Supplements supplements;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplements createEntity() {
        Supplements supplements = new Supplements().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return supplements;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplements createUpdatedEntity() {
        Supplements supplements = new Supplements().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return supplements;
    }

    @BeforeEach
    public void initTest() {
        supplementsRepository.deleteAll().block();
        supplements = createEntity();
    }

    @Test
    void createSupplements() throws Exception {
        int databaseSizeBeforeCreate = supplementsRepository.findAll().collectList().block().size();
        // Create the Supplements
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeCreate + 1);
        Supplements testSupplements = supplementsList.get(supplementsList.size() - 1);
        assertThat(testSupplements.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplements.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createSupplementsWithExistingId() throws Exception {
        // Create the Supplements with an existing ID
        supplements.setId("existing_id");

        int databaseSizeBeforeCreate = supplementsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSupplements() {
        // Initialize the database
        supplementsRepository.save(supplements).block();

        // Get all the supplementsList
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
            .value(hasItem(supplements.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getSupplements() {
        // Initialize the database
        supplementsRepository.save(supplements).block();

        // Get the supplements
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, supplements.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(supplements.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getNonExistingSupplements() {
        // Get the supplements
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSupplements() throws Exception {
        // Initialize the database
        supplementsRepository.save(supplements).block();

        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();

        // Update the supplements
        Supplements updatedSupplements = supplementsRepository.findById(supplements.getId()).block();
        updatedSupplements.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSupplements.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSupplements))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
        Supplements testSupplements = supplementsList.get(supplementsList.size() - 1);
        assertThat(testSupplements.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingSupplements() throws Exception {
        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();
        supplements.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, supplements.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSupplements() throws Exception {
        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();
        supplements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSupplements() throws Exception {
        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();
        supplements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSupplementsWithPatch() throws Exception {
        // Initialize the database
        supplementsRepository.save(supplements).block();

        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();

        // Update the supplements using partial update
        Supplements partialUpdatedSupplements = new Supplements();
        partialUpdatedSupplements.setId(supplements.getId());

        partialUpdatedSupplements.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSupplements.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplements))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
        Supplements testSupplements = supplementsList.get(supplementsList.size() - 1);
        assertThat(testSupplements.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplements.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateSupplementsWithPatch() throws Exception {
        // Initialize the database
        supplementsRepository.save(supplements).block();

        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();

        // Update the supplements using partial update
        Supplements partialUpdatedSupplements = new Supplements();
        partialUpdatedSupplements.setId(supplements.getId());

        partialUpdatedSupplements.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSupplements.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplements))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
        Supplements testSupplements = supplementsList.get(supplementsList.size() - 1);
        assertThat(testSupplements.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingSupplements() throws Exception {
        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();
        supplements.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, supplements.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSupplements() throws Exception {
        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();
        supplements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSupplements() throws Exception {
        int databaseSizeBeforeUpdate = supplementsRepository.findAll().collectList().block().size();
        supplements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(supplements))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Supplements in the database
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSupplements() {
        // Initialize the database
        supplementsRepository.save(supplements).block();

        int databaseSizeBeforeDelete = supplementsRepository.findAll().collectList().block().size();

        // Delete the supplements
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, supplements.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Supplements> supplementsList = supplementsRepository.findAll().collectList().block();
        assertThat(supplementsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
