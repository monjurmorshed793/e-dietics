package org.morshed.repository;

import org.morshed.domain.ComponentNavigation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ComponentNavigation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComponentNavigationRepository extends ReactiveMongoRepository<ComponentNavigation, String> {
    Flux<ComponentNavigation> findAllBy(Pageable pageable);
}
