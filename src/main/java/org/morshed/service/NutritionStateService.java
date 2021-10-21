package org.morshed.service;

import org.morshed.domain.NutritionState;
import org.morshed.repository.NutritionStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link NutritionState}.
 */
@Service
public class NutritionStateService {

    private final Logger log = LoggerFactory.getLogger(NutritionStateService.class);

    private final NutritionStateRepository nutritionStateRepository;

    public NutritionStateService(NutritionStateRepository nutritionStateRepository) {
        this.nutritionStateRepository = nutritionStateRepository;
    }

    /**
     * Save a nutritionState.
     *
     * @param nutritionState the entity to save.
     * @return the persisted entity.
     */
    public Mono<NutritionState> save(NutritionState nutritionState) {
        log.debug("Request to save NutritionState : {}", nutritionState);
        return nutritionStateRepository.save(nutritionState);
    }

    /**
     * Partially update a nutritionState.
     *
     * @param nutritionState the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<NutritionState> partialUpdate(NutritionState nutritionState) {
        log.debug("Request to partially update NutritionState : {}", nutritionState);

        return nutritionStateRepository
            .findById(nutritionState.getId())
            .map(existingNutritionState -> {
                if (nutritionState.getOrder() != null) {
                    existingNutritionState.setOrder(nutritionState.getOrder());
                }
                if (nutritionState.getLabel() != null) {
                    existingNutritionState.setLabel(nutritionState.getLabel());
                }
                if (nutritionState.getNote() != null) {
                    existingNutritionState.setNote(nutritionState.getNote());
                }

                return existingNutritionState;
            })
            .flatMap(nutritionStateRepository::save);
    }

    /**
     * Get all the nutritionStates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<NutritionState> findAll(Pageable pageable) {
        log.debug("Request to get all NutritionStates");
        return nutritionStateRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of nutritionStates available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return nutritionStateRepository.count();
    }

    /**
     * Get one nutritionState by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<NutritionState> findOne(String id) {
        log.debug("Request to get NutritionState : {}", id);
        return nutritionStateRepository.findById(id);
    }

    /**
     * Delete the nutritionState by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete NutritionState : {}", id);
        return nutritionStateRepository.deleteById(id);
    }
}
