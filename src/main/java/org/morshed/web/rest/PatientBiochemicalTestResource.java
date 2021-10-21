package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.morshed.domain.PatientBiochemicalTest;
import org.morshed.repository.PatientBiochemicalTestRepository;
import org.morshed.service.PatientBiochemicalTestService;
import org.morshed.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link org.morshed.domain.PatientBiochemicalTest}.
 */
@RestController
@RequestMapping("/api")
public class PatientBiochemicalTestResource {

    private final Logger log = LoggerFactory.getLogger(PatientBiochemicalTestResource.class);

    private static final String ENTITY_NAME = "patientBiochemicalTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientBiochemicalTestService patientBiochemicalTestService;

    private final PatientBiochemicalTestRepository patientBiochemicalTestRepository;

    public PatientBiochemicalTestResource(
        PatientBiochemicalTestService patientBiochemicalTestService,
        PatientBiochemicalTestRepository patientBiochemicalTestRepository
    ) {
        this.patientBiochemicalTestService = patientBiochemicalTestService;
        this.patientBiochemicalTestRepository = patientBiochemicalTestRepository;
    }

    /**
     * {@code POST  /patient-biochemical-tests} : Create a new patientBiochemicalTest.
     *
     * @param patientBiochemicalTest the patientBiochemicalTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientBiochemicalTest, or with status {@code 400 (Bad Request)} if the patientBiochemicalTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-biochemical-tests")
    public Mono<ResponseEntity<PatientBiochemicalTest>> createPatientBiochemicalTest(
        @RequestBody PatientBiochemicalTest patientBiochemicalTest
    ) throws URISyntaxException {
        log.debug("REST request to save PatientBiochemicalTest : {}", patientBiochemicalTest);
        if (patientBiochemicalTest.getId() != null) {
            throw new BadRequestAlertException("A new patientBiochemicalTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return patientBiochemicalTestService
            .save(patientBiochemicalTest)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/patient-biochemical-tests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /patient-biochemical-tests/:id} : Updates an existing patientBiochemicalTest.
     *
     * @param id the id of the patientBiochemicalTest to save.
     * @param patientBiochemicalTest the patientBiochemicalTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientBiochemicalTest,
     * or with status {@code 400 (Bad Request)} if the patientBiochemicalTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientBiochemicalTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-biochemical-tests/{id}")
    public Mono<ResponseEntity<PatientBiochemicalTest>> updatePatientBiochemicalTest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PatientBiochemicalTest patientBiochemicalTest
    ) throws URISyntaxException {
        log.debug("REST request to update PatientBiochemicalTest : {}, {}", id, patientBiochemicalTest);
        if (patientBiochemicalTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientBiochemicalTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return patientBiochemicalTestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return patientBiochemicalTestService
                    .save(patientBiochemicalTest)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /patient-biochemical-tests/:id} : Partial updates given fields of an existing patientBiochemicalTest, field will ignore if it is null
     *
     * @param id the id of the patientBiochemicalTest to save.
     * @param patientBiochemicalTest the patientBiochemicalTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientBiochemicalTest,
     * or with status {@code 400 (Bad Request)} if the patientBiochemicalTest is not valid,
     * or with status {@code 404 (Not Found)} if the patientBiochemicalTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientBiochemicalTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-biochemical-tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PatientBiochemicalTest>> partialUpdatePatientBiochemicalTest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PatientBiochemicalTest patientBiochemicalTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientBiochemicalTest partially : {}, {}", id, patientBiochemicalTest);
        if (patientBiochemicalTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientBiochemicalTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return patientBiochemicalTestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PatientBiochemicalTest> result = patientBiochemicalTestService.partialUpdate(patientBiochemicalTest);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /patient-biochemical-tests} : get all the patientBiochemicalTests.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientBiochemicalTests in body.
     */
    @GetMapping("/patient-biochemical-tests")
    public Mono<ResponseEntity<List<PatientBiochemicalTest>>> getAllPatientBiochemicalTests(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of PatientBiochemicalTests");
        return patientBiochemicalTestService
            .countAll()
            .zipWith(patientBiochemicalTestService.findAll(pageable).collectList())
            .map(countWithEntities -> {
                return ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2());
            });
    }

    /**
     * {@code GET  /patient-biochemical-tests/:id} : get the "id" patientBiochemicalTest.
     *
     * @param id the id of the patientBiochemicalTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientBiochemicalTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-biochemical-tests/{id}")
    public Mono<ResponseEntity<PatientBiochemicalTest>> getPatientBiochemicalTest(@PathVariable String id) {
        log.debug("REST request to get PatientBiochemicalTest : {}", id);
        Mono<PatientBiochemicalTest> patientBiochemicalTest = patientBiochemicalTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientBiochemicalTest);
    }

    /**
     * {@code DELETE  /patient-biochemical-tests/:id} : delete the "id" patientBiochemicalTest.
     *
     * @param id the id of the patientBiochemicalTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-biochemical-tests/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePatientBiochemicalTest(@PathVariable String id) {
        log.debug("REST request to delete PatientBiochemicalTest : {}", id);
        return patientBiochemicalTestService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
