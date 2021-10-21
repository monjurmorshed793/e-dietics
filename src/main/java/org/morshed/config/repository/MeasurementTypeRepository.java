package org.morshed.config.repository;

import org.morshed.config.domain.MeasurementType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the MeasurementType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasurementTypeRepository extends ReactiveMongoRepository<MeasurementType, String> {
    Flux<MeasurementType> findAllBy(Pageable pageable);
}
