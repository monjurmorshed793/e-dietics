package org.morshed.config.service;

import org.morshed.config.domain.DietNature;
import org.morshed.config.repository.DietNatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DietNature}.
 */
@Service
public class DietNatureService {

    private final Logger log = LoggerFactory.getLogger(DietNatureService.class);

    private final DietNatureRepository dietNatureRepository;

    public DietNatureService(DietNatureRepository dietNatureRepository) {
        this.dietNatureRepository = dietNatureRepository;
    }

    /**
     * Save a dietNature.
     *
     * @param dietNature the entity to save.
     * @return the persisted entity.
     */
    public Mono<DietNature> save(DietNature dietNature) {
        log.debug("Request to save DietNature : {}", dietNature);
        return dietNatureRepository.save(dietNature);
    }

    /**
     * Partially update a dietNature.
     *
     * @param dietNature the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DietNature> partialUpdate(DietNature dietNature) {
        log.debug("Request to partially update DietNature : {}", dietNature);

        return dietNatureRepository
            .findById(dietNature.getId())
            .map(existingDietNature -> {
                if (dietNature.getName() != null) {
                    existingDietNature.setName(dietNature.getName());
                }
                if (dietNature.getDescription() != null) {
                    existingDietNature.setDescription(dietNature.getDescription());
                }

                return existingDietNature;
            })
            .flatMap(dietNatureRepository::save);
    }

    /**
     * Get all the dietNatures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<DietNature> findAll(Pageable pageable) {
        log.debug("Request to get all DietNatures");
        return dietNatureRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of dietNatures available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return dietNatureRepository.count();
    }

    /**
     * Get one dietNature by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<DietNature> findOne(String id) {
        log.debug("Request to get DietNature : {}", id);
        return dietNatureRepository.findById(id);
    }

    /**
     * Delete the dietNature by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete DietNature : {}", id);
        return dietNatureRepository.deleteById(id);
    }
}
