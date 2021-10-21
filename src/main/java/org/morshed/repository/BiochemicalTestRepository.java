package org.morshed.repository;

import org.morshed.domain.BiochemicalTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the BiochemicalTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BiochemicalTestRepository extends ReactiveMongoRepository<BiochemicalTest, String> {
    Flux<BiochemicalTest> findAllBy(Pageable pageable);
}
