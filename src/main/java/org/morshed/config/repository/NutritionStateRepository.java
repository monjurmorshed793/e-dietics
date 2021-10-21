package org.morshed.config.repository;

import org.morshed.config.domain.NutritionState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the NutritionState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NutritionStateRepository extends ReactiveMongoRepository<NutritionState, String> {
    Flux<NutritionState> findAllBy(Pageable pageable);
}
