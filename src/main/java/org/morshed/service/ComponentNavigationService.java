package org.morshed.service;

import org.morshed.domain.ComponentNavigation;
import org.morshed.repository.ComponentNavigationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ComponentNavigation}.
 */
@Service
public class ComponentNavigationService {

    private final Logger log = LoggerFactory.getLogger(ComponentNavigationService.class);

    private final ComponentNavigationRepository componentNavigationRepository;

    public ComponentNavigationService(ComponentNavigationRepository componentNavigationRepository) {
        this.componentNavigationRepository = componentNavigationRepository;
    }

    /**
     * Save a componentNavigation.
     *
     * @param componentNavigation the entity to save.
     * @return the persisted entity.
     */
    public Mono<ComponentNavigation> save(ComponentNavigation componentNavigation) {
        log.debug("Request to save ComponentNavigation : {}", componentNavigation);
        return componentNavigationRepository.save(componentNavigation);
    }

    /**
     * Partially update a componentNavigation.
     *
     * @param componentNavigation the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ComponentNavigation> partialUpdate(ComponentNavigation componentNavigation) {
        log.debug("Request to partially update ComponentNavigation : {}", componentNavigation);

        return componentNavigationRepository
            .findById(componentNavigation.getId())
            .map(existingComponentNavigation -> {
                if (componentNavigation.getName() != null) {
                    existingComponentNavigation.setName(componentNavigation.getName());
                }
                if (componentNavigation.getLocation() != null) {
                    existingComponentNavigation.setLocation(componentNavigation.getLocation());
                }

                return existingComponentNavigation;
            })
            .flatMap(componentNavigationRepository::save);
    }

    /**
     * Get all the componentNavigations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ComponentNavigation> findAll(Pageable pageable) {
        log.debug("Request to get all ComponentNavigations");
        return componentNavigationRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of componentNavigations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return componentNavigationRepository.count();
    }

    /**
     * Get one componentNavigation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ComponentNavigation> findOne(String id) {
        log.debug("Request to get ComponentNavigation : {}", id);
        return componentNavigationRepository.findById(id);
    }

    /**
     * Delete the componentNavigation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ComponentNavigation : {}", id);
        return componentNavigationRepository.deleteById(id);
    }
}
