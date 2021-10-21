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
import org.morshed.domain.FoodCategory;
import org.morshed.repository.FoodCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FoodCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FoodCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/food-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private WebTestClient webTestClient;

    private FoodCategory foodCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodCategory createEntity() {
        FoodCategory foodCategory = new FoodCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return foodCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodCategory createUpdatedEntity() {
        FoodCategory foodCategory = new FoodCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return foodCategory;
    }

    @BeforeEach
    public void initTest() {
        foodCategoryRepository.deleteAll().block();
        foodCategory = createEntity();
    }

    @Test
    void createFoodCategory() throws Exception {
        int databaseSizeBeforeCreate = foodCategoryRepository.findAll().collectList().block().size();
        // Create the FoodCategory
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        FoodCategory testFoodCategory = foodCategoryList.get(foodCategoryList.size() - 1);
        assertThat(testFoodCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFoodCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createFoodCategoryWithExistingId() throws Exception {
        // Create the FoodCategory with an existing ID
        foodCategory.setId("existing_id");

        int databaseSizeBeforeCreate = foodCategoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFoodCategories() {
        // Initialize the database
        foodCategoryRepository.save(foodCategory).block();

        // Get all the foodCategoryList
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
            .value(hasItem(foodCategory.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getFoodCategory() {
        // Initialize the database
        foodCategoryRepository.save(foodCategory).block();

        // Get the foodCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, foodCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(foodCategory.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    void getNonExistingFoodCategory() {
        // Get the foodCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFoodCategory() throws Exception {
        // Initialize the database
        foodCategoryRepository.save(foodCategory).block();

        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();

        // Update the foodCategory
        FoodCategory updatedFoodCategory = foodCategoryRepository.findById(foodCategory.getId()).block();
        updatedFoodCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFoodCategory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFoodCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
        FoodCategory testFoodCategory = foodCategoryList.get(foodCategoryList.size() - 1);
        assertThat(testFoodCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoodCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingFoodCategory() throws Exception {
        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();
        foodCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, foodCategory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFoodCategory() throws Exception {
        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();
        foodCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFoodCategory() throws Exception {
        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();
        foodCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFoodCategoryWithPatch() throws Exception {
        // Initialize the database
        foodCategoryRepository.save(foodCategory).block();

        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();

        // Update the foodCategory using partial update
        FoodCategory partialUpdatedFoodCategory = new FoodCategory();
        partialUpdatedFoodCategory.setId(foodCategory.getId());

        partialUpdatedFoodCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFoodCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
        FoodCategory testFoodCategory = foodCategoryList.get(foodCategoryList.size() - 1);
        assertThat(testFoodCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoodCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateFoodCategoryWithPatch() throws Exception {
        // Initialize the database
        foodCategoryRepository.save(foodCategory).block();

        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();

        // Update the foodCategory using partial update
        FoodCategory partialUpdatedFoodCategory = new FoodCategory();
        partialUpdatedFoodCategory.setId(foodCategory.getId());

        partialUpdatedFoodCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFoodCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
        FoodCategory testFoodCategory = foodCategoryList.get(foodCategoryList.size() - 1);
        assertThat(testFoodCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoodCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingFoodCategory() throws Exception {
        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();
        foodCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, foodCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFoodCategory() throws Exception {
        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();
        foodCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFoodCategory() throws Exception {
        int databaseSizeBeforeUpdate = foodCategoryRepository.findAll().collectList().block().size();
        foodCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(foodCategory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FoodCategory in the database
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFoodCategory() {
        // Initialize the database
        foodCategoryRepository.save(foodCategory).block();

        int databaseSizeBeforeDelete = foodCategoryRepository.findAll().collectList().block().size();

        // Delete the foodCategory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, foodCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FoodCategory> foodCategoryList = foodCategoryRepository.findAll().collectList().block();
        assertThat(foodCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
