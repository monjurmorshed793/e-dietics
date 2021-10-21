package org.morshed.repository;

import org.morshed.domain.FoodCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the FoodCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodCategoryRepository extends ReactiveMongoRepository<FoodCategory, String> {
    Flux<FoodCategory> findAllBy(Pageable pageable);
}
