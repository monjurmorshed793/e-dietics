package org.morshed.repository;

import org.morshed.domain.DietNature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the DietNature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietNatureRepository extends ReactiveMongoRepository<DietNature, String> {
    Flux<DietNature> findAllBy(Pageable pageable);
}
