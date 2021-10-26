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
import org.morshed.domain.ComponentNavigation;
import org.morshed.repository.ComponentNavigationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ComponentNavigationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ComponentNavigationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_ROLES = "AAAAAAAAAA";
    private static final String UPDATED_ROLES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/component-navigations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ComponentNavigationRepository componentNavigationRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ComponentNavigation componentNavigation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentNavigation createEntity() {
        ComponentNavigation componentNavigation = new ComponentNavigation()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .roles(DEFAULT_ROLES);
        return componentNavigation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentNavigation createUpdatedEntity() {
        ComponentNavigation componentNavigation = new ComponentNavigation()
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .roles(UPDATED_ROLES);
        return componentNavigation;
    }

    @BeforeEach
    public void initTest() {
        componentNavigationRepository.deleteAll().block();
        componentNavigation = createEntity();
    }

    @Test
    void createComponentNavigation() throws Exception {
        int databaseSizeBeforeCreate = componentNavigationRepository.findAll().collectList().block().size();
        // Create the ComponentNavigation
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeCreate + 1);
        ComponentNavigation testComponentNavigation = componentNavigationList.get(componentNavigationList.size() - 1);
        assertThat(testComponentNavigation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testComponentNavigation.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testComponentNavigation.getRoles()).isEqualTo(DEFAULT_ROLES);
    }

    @Test
    void createComponentNavigationWithExistingId() throws Exception {
        // Create the ComponentNavigation with an existing ID
        componentNavigation.setId("existing_id");

        int databaseSizeBeforeCreate = componentNavigationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentNavigationRepository.findAll().collectList().block().size();
        // set the field null
        componentNavigation.setName(null);

        // Create the ComponentNavigation, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = componentNavigationRepository.findAll().collectList().block().size();
        // set the field null
        componentNavigation.setLocation(null);

        // Create the ComponentNavigation, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllComponentNavigations() {
        // Initialize the database
        componentNavigationRepository.save(componentNavigation).block();

        // Get all the componentNavigationList
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
            .value(hasItem(componentNavigation.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].roles")
            .value(hasItem(DEFAULT_ROLES));
    }

    @Test
    void getComponentNavigation() {
        // Initialize the database
        componentNavigationRepository.save(componentNavigation).block();

        // Get the componentNavigation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, componentNavigation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(componentNavigation.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.roles")
            .value(is(DEFAULT_ROLES));
    }

    @Test
    void getNonExistingComponentNavigation() {
        // Get the componentNavigation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewComponentNavigation() throws Exception {
        // Initialize the database
        componentNavigationRepository.save(componentNavigation).block();

        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();

        // Update the componentNavigation
        ComponentNavigation updatedComponentNavigation = componentNavigationRepository.findById(componentNavigation.getId()).block();
        updatedComponentNavigation.name(UPDATED_NAME).location(UPDATED_LOCATION).roles(UPDATED_ROLES);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedComponentNavigation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedComponentNavigation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
        ComponentNavigation testComponentNavigation = componentNavigationList.get(componentNavigationList.size() - 1);
        assertThat(testComponentNavigation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testComponentNavigation.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testComponentNavigation.getRoles()).isEqualTo(UPDATED_ROLES);
    }

    @Test
    void putNonExistingComponentNavigation() throws Exception {
        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();
        componentNavigation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, componentNavigation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchComponentNavigation() throws Exception {
        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();
        componentNavigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamComponentNavigation() throws Exception {
        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();
        componentNavigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateComponentNavigationWithPatch() throws Exception {
        // Initialize the database
        componentNavigationRepository.save(componentNavigation).block();

        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();

        // Update the componentNavigation using partial update
        ComponentNavigation partialUpdatedComponentNavigation = new ComponentNavigation();
        partialUpdatedComponentNavigation.setId(componentNavigation.getId());

        partialUpdatedComponentNavigation.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedComponentNavigation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentNavigation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
        ComponentNavigation testComponentNavigation = componentNavigationList.get(componentNavigationList.size() - 1);
        assertThat(testComponentNavigation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testComponentNavigation.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testComponentNavigation.getRoles()).isEqualTo(DEFAULT_ROLES);
    }

    @Test
    void fullUpdateComponentNavigationWithPatch() throws Exception {
        // Initialize the database
        componentNavigationRepository.save(componentNavigation).block();

        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();

        // Update the componentNavigation using partial update
        ComponentNavigation partialUpdatedComponentNavigation = new ComponentNavigation();
        partialUpdatedComponentNavigation.setId(componentNavigation.getId());

        partialUpdatedComponentNavigation.name(UPDATED_NAME).location(UPDATED_LOCATION).roles(UPDATED_ROLES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedComponentNavigation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentNavigation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
        ComponentNavigation testComponentNavigation = componentNavigationList.get(componentNavigationList.size() - 1);
        assertThat(testComponentNavigation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testComponentNavigation.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testComponentNavigation.getRoles()).isEqualTo(UPDATED_ROLES);
    }

    @Test
    void patchNonExistingComponentNavigation() throws Exception {
        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();
        componentNavigation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, componentNavigation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchComponentNavigation() throws Exception {
        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();
        componentNavigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamComponentNavigation() throws Exception {
        int databaseSizeBeforeUpdate = componentNavigationRepository.findAll().collectList().block().size();
        componentNavigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(componentNavigation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ComponentNavigation in the database
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteComponentNavigation() {
        // Initialize the database
        componentNavigationRepository.save(componentNavigation).block();

        int databaseSizeBeforeDelete = componentNavigationRepository.findAll().collectList().block().size();

        // Delete the componentNavigation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, componentNavigation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ComponentNavigation> componentNavigationList = componentNavigationRepository.findAll().collectList().block();
        assertThat(componentNavigationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
