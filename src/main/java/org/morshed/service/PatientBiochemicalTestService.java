package org.morshed.service;

import org.morshed.domain.PatientBiochemicalTest;
import org.morshed.repository.PatientBiochemicalTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PatientBiochemicalTest}.
 */
@Service
public class PatientBiochemicalTestService {

    private final Logger log = LoggerFactory.getLogger(PatientBiochemicalTestService.class);

    private final PatientBiochemicalTestRepository patientBiochemicalTestRepository;

    public PatientBiochemicalTestService(PatientBiochemicalTestRepository patientBiochemicalTestRepository) {
        this.patientBiochemicalTestRepository = patientBiochemicalTestRepository;
    }

    /**
     * Save a patientBiochemicalTest.
     *
     * @param patientBiochemicalTest the entity to save.
     * @return the persisted entity.
     */
    public Mono<PatientBiochemicalTest> save(PatientBiochemicalTest patientBiochemicalTest) {
        log.debug("Request to save PatientBiochemicalTest : {}", patientBiochemicalTest);
        return patientBiochemicalTestRepository.save(patientBiochemicalTest);
    }

    /**
     * Partially update a patientBiochemicalTest.
     *
     * @param patientBiochemicalTest the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PatientBiochemicalTest> partialUpdate(PatientBiochemicalTest patientBiochemicalTest) {
        log.debug("Request to partially update PatientBiochemicalTest : {}", patientBiochemicalTest);

        return patientBiochemicalTestRepository
            .findById(patientBiochemicalTest.getId())
            .map(existingPatientBiochemicalTest -> {
                if (patientBiochemicalTest.getOther() != null) {
                    existingPatientBiochemicalTest.setOther(patientBiochemicalTest.getOther());
                }
                if (patientBiochemicalTest.getValue() != null) {
                    existingPatientBiochemicalTest.setValue(patientBiochemicalTest.getValue());
                }

                return existingPatientBiochemicalTest;
            })
            .flatMap(patientBiochemicalTestRepository::save);
    }

    /**
     * Get all the patientBiochemicalTests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<PatientBiochemicalTest> findAll(Pageable pageable) {
        log.debug("Request to get all PatientBiochemicalTests");
        return patientBiochemicalTestRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of patientBiochemicalTests available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return patientBiochemicalTestRepository.count();
    }

    /**
     * Get one patientBiochemicalTest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<PatientBiochemicalTest> findOne(String id) {
        log.debug("Request to get PatientBiochemicalTest : {}", id);
        return patientBiochemicalTestRepository.findById(id);
    }

    /**
     * Delete the patientBiochemicalTest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete PatientBiochemicalTest : {}", id);
        return patientBiochemicalTestRepository.deleteById(id);
    }
}
