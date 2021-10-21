package org.morshed.config.repository;

import org.morshed.config.domain.Supplements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Supplements entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplementsRepository extends ReactiveMongoRepository<Supplements, String> {
    Flux<Supplements> findAllBy(Pageable pageable);
}
