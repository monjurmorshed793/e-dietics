package org.morshed.service;

import org.morshed.domain.BiochemicalTest;
import org.morshed.repository.BiochemicalTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link BiochemicalTest}.
 */
@Service
public class BiochemicalTestService {

    private final Logger log = LoggerFactory.getLogger(BiochemicalTestService.class);

    private final BiochemicalTestRepository biochemicalTestRepository;

    public BiochemicalTestService(BiochemicalTestRepository biochemicalTestRepository) {
        this.biochemicalTestRepository = biochemicalTestRepository;
    }

    /**
     * Save a biochemicalTest.
     *
     * @param biochemicalTest the entity to save.
     * @return the persisted entity.
     */
    public Mono<BiochemicalTest> save(BiochemicalTest biochemicalTest) {
        log.debug("Request to save BiochemicalTest : {}", biochemicalTest);
        return biochemicalTestRepository.save(biochemicalTest);
    }

    /**
     * Partially update a biochemicalTest.
     *
     * @param biochemicalTest the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BiochemicalTest> partialUpdate(BiochemicalTest biochemicalTest) {
        log.debug("Request to partially update BiochemicalTest : {}", biochemicalTest);

        return biochemicalTestRepository
            .findById(biochemicalTest.getId())
            .map(existingBiochemicalTest -> {
                if (biochemicalTest.getTestName() != null) {
                    existingBiochemicalTest.setTestName(biochemicalTest.getTestName());
                }
                if (biochemicalTest.getDescription() != null) {
                    existingBiochemicalTest.setDescription(biochemicalTest.getDescription());
                }

                return existingBiochemicalTest;
            })
            .flatMap(biochemicalTestRepository::save);
    }

    /**
     * Get all the biochemicalTests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<BiochemicalTest> findAll(Pageable pageable) {
        log.debug("Request to get all BiochemicalTests");
        return biochemicalTestRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of biochemicalTests available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return biochemicalTestRepository.count();
    }

    /**
     * Get one biochemicalTest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<BiochemicalTest> findOne(String id) {
        log.debug("Request to get BiochemicalTest : {}", id);
        return biochemicalTestRepository.findById(id);
    }

    /**
     * Delete the biochemicalTest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete BiochemicalTest : {}", id);
        return biochemicalTestRepository.deleteById(id);
    }
}
