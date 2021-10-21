package org.morshed.config.repository;

import org.morshed.config.domain.ActivityLevel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ActivityLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityLevelRepository extends ReactiveMongoRepository<ActivityLevel, String> {
    Flux<ActivityLevel> findAllBy(Pageable pageable);
}
