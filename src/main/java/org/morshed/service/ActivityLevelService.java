package org.morshed.service;

import org.morshed.domain.ActivityLevel;
import org.morshed.repository.ActivityLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ActivityLevel}.
 */
@Service
public class ActivityLevelService {

    private final Logger log = LoggerFactory.getLogger(ActivityLevelService.class);

    private final ActivityLevelRepository activityLevelRepository;

    public ActivityLevelService(ActivityLevelRepository activityLevelRepository) {
        this.activityLevelRepository = activityLevelRepository;
    }

    /**
     * Save a activityLevel.
     *
     * @param activityLevel the entity to save.
     * @return the persisted entity.
     */
    public Mono<ActivityLevel> save(ActivityLevel activityLevel) {
        log.debug("Request to save ActivityLevel : {}", activityLevel);
        return activityLevelRepository.save(activityLevel);
    }

    /**
     * Partially update a activityLevel.
     *
     * @param activityLevel the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ActivityLevel> partialUpdate(ActivityLevel activityLevel) {
        log.debug("Request to partially update ActivityLevel : {}", activityLevel);

        return activityLevelRepository
            .findById(activityLevel.getId())
            .map(existingActivityLevel -> {
                if (activityLevel.getOrder() != null) {
                    existingActivityLevel.setOrder(activityLevel.getOrder());
                }
                if (activityLevel.getLabel() != null) {
                    existingActivityLevel.setLabel(activityLevel.getLabel());
                }
                if (activityLevel.getNote() != null) {
                    existingActivityLevel.setNote(activityLevel.getNote());
                }

                return existingActivityLevel;
            })
            .flatMap(activityLevelRepository::save);
    }

    /**
     * Get all the activityLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ActivityLevel> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityLevels");
        return activityLevelRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of activityLevels available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return activityLevelRepository.count();
    }

    /**
     * Get one activityLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ActivityLevel> findOne(String id) {
        log.debug("Request to get ActivityLevel : {}", id);
        return activityLevelRepository.findById(id);
    }

    /**
     * Delete the activityLevel by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ActivityLevel : {}", id);
        return activityLevelRepository.deleteById(id);
    }
}
