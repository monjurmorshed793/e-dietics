package org.morshed.patient.repository;

import org.morshed.patient.domain.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends ReactiveMongoRepository<Patient, String> {
    Flux<Patient> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Patient> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Patient> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Patient> findOneWithEagerRelationships(String id);
}
