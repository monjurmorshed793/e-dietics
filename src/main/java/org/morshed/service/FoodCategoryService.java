package org.morshed.service;

import org.morshed.domain.FoodCategory;
import org.morshed.repository.FoodCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link FoodCategory}.
 */
@Service
public class FoodCategoryService {

    private final Logger log = LoggerFactory.getLogger(FoodCategoryService.class);

    private final FoodCategoryRepository foodCategoryRepository;

    public FoodCategoryService(FoodCategoryRepository foodCategoryRepository) {
        this.foodCategoryRepository = foodCategoryRepository;
    }

    /**
     * Save a foodCategory.
     *
     * @param foodCategory the entity to save.
     * @return the persisted entity.
     */
    public Mono<FoodCategory> save(FoodCategory foodCategory) {
        log.debug("Request to save FoodCategory : {}", foodCategory);
        return foodCategoryRepository.save(foodCategory);
    }

    /**
     * Partially update a foodCategory.
     *
     * @param foodCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FoodCategory> partialUpdate(FoodCategory foodCategory) {
        log.debug("Request to partially update FoodCategory : {}", foodCategory);

        return foodCategoryRepository
            .findById(foodCategory.getId())
            .map(existingFoodCategory -> {
                if (foodCategory.getName() != null) {
                    existingFoodCategory.setName(foodCategory.getName());
                }
                if (foodCategory.getDescription() != null) {
                    existingFoodCategory.setDescription(foodCategory.getDescription());
                }

                return existingFoodCategory;
            })
            .flatMap(foodCategoryRepository::save);
    }

    /**
     * Get all the foodCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FoodCategory> findAll(Pageable pageable) {
        log.debug("Request to get all FoodCategories");
        return foodCategoryRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of foodCategories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return foodCategoryRepository.count();
    }

    /**
     * Get one foodCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<FoodCategory> findOne(String id) {
        log.debug("Request to get FoodCategory : {}", id);
        return foodCategoryRepository.findById(id);
    }

    /**
     * Delete the foodCategory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete FoodCategory : {}", id);
        return foodCategoryRepository.deleteById(id);
    }
}
