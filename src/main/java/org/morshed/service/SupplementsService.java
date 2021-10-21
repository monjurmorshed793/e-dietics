package org.morshed.service;

import org.morshed.domain.Supplements;
import org.morshed.repository.SupplementsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Supplements}.
 */
@Service
public class SupplementsService {

    private final Logger log = LoggerFactory.getLogger(SupplementsService.class);

    private final SupplementsRepository supplementsRepository;

    public SupplementsService(SupplementsRepository supplementsRepository) {
        this.supplementsRepository = supplementsRepository;
    }

    /**
     * Save a supplements.
     *
     * @param supplements the entity to save.
     * @return the persisted entity.
     */
    public Mono<Supplements> save(Supplements supplements) {
        log.debug("Request to save Supplements : {}", supplements);
        return supplementsRepository.save(supplements);
    }

    /**
     * Partially update a supplements.
     *
     * @param supplements the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Supplements> partialUpdate(Supplements supplements) {
        log.debug("Request to partially update Supplements : {}", supplements);

        return supplementsRepository
            .findById(supplements.getId())
            .map(existingSupplements -> {
                if (supplements.getName() != null) {
                    existingSupplements.setName(supplements.getName());
                }
                if (supplements.getDescription() != null) {
                    existingSupplements.setDescription(supplements.getDescription());
                }

                return existingSupplements;
            })
            .flatMap(supplementsRepository::save);
    }

    /**
     * Get all the supplements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<Supplements> findAll(Pageable pageable) {
        log.debug("Request to get all Supplements");
        return supplementsRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of supplements available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return supplementsRepository.count();
    }

    /**
     * Get one supplements by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<Supplements> findOne(String id) {
        log.debug("Request to get Supplements : {}", id);
        return supplementsRepository.findById(id);
    }

    /**
     * Delete the supplements by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Supplements : {}", id);
        return supplementsRepository.deleteById(id);
    }
}
