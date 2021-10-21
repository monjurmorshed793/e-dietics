package org.morshed.config.service;

import org.morshed.config.domain.MeasurementType;
import org.morshed.config.repository.MeasurementTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link MeasurementType}.
 */
@Service
public class MeasurementTypeService {

    private final Logger log = LoggerFactory.getLogger(MeasurementTypeService.class);

    private final MeasurementTypeRepository measurementTypeRepository;

    public MeasurementTypeService(MeasurementTypeRepository measurementTypeRepository) {
        this.measurementTypeRepository = measurementTypeRepository;
    }

    /**
     * Save a measurementType.
     *
     * @param measurementType the entity to save.
     * @return the persisted entity.
     */
    public Mono<MeasurementType> save(MeasurementType measurementType) {
        log.debug("Request to save MeasurementType : {}", measurementType);
        return measurementTypeRepository.save(measurementType);
    }

    /**
     * Partially update a measurementType.
     *
     * @param measurementType the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MeasurementType> partialUpdate(MeasurementType measurementType) {
        log.debug("Request to partially update MeasurementType : {}", measurementType);

        return measurementTypeRepository
            .findById(measurementType.getId())
            .map(existingMeasurementType -> {
                if (measurementType.getLabel() != null) {
                    existingMeasurementType.setLabel(measurementType.getLabel());
                }
                if (measurementType.getDescription() != null) {
                    existingMeasurementType.setDescription(measurementType.getDescription());
                }

                return existingMeasurementType;
            })
            .flatMap(measurementTypeRepository::save);
    }

    /**
     * Get all the measurementTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<MeasurementType> findAll(Pageable pageable) {
        log.debug("Request to get all MeasurementTypes");
        return measurementTypeRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of measurementTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return measurementTypeRepository.count();
    }

    /**
     * Get one measurementType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<MeasurementType> findOne(String id) {
        log.debug("Request to get MeasurementType : {}", id);
        return measurementTypeRepository.findById(id);
    }

    /**
     * Delete the measurementType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete MeasurementType : {}", id);
        return measurementTypeRepository.deleteById(id);
    }
}
