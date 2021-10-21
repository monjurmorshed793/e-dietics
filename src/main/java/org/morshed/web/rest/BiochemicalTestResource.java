package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.morshed.domain.BiochemicalTest;
import org.morshed.repository.BiochemicalTestRepository;
import org.morshed.service.BiochemicalTestService;
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
 * REST controller for managing {@link org.morshed.domain.BiochemicalTest}.
 */
@RestController
@RequestMapping("/api")
public class BiochemicalTestResource {

    private final Logger log = LoggerFactory.getLogger(BiochemicalTestResource.class);

    private static final String ENTITY_NAME = "biochemicalTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BiochemicalTestService biochemicalTestService;

    private final BiochemicalTestRepository biochemicalTestRepository;

    public BiochemicalTestResource(BiochemicalTestService biochemicalTestService, BiochemicalTestRepository biochemicalTestRepository) {
        this.biochemicalTestService = biochemicalTestService;
        this.biochemicalTestRepository = biochemicalTestRepository;
    }

    /**
     * {@code POST  /biochemical-tests} : Create a new biochemicalTest.
     *
     * @param biochemicalTest the biochemicalTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new biochemicalTest, or with status {@code 400 (Bad Request)} if the biochemicalTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/biochemical-tests")
    public Mono<ResponseEntity<BiochemicalTest>> createBiochemicalTest(@RequestBody BiochemicalTest biochemicalTest)
        throws URISyntaxException {
        log.debug("REST request to save BiochemicalTest : {}", biochemicalTest);
        if (biochemicalTest.getId() != null) {
            throw new BadRequestAlertException("A new biochemicalTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return biochemicalTestService
            .save(biochemicalTest)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/biochemical-tests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /biochemical-tests/:id} : Updates an existing biochemicalTest.
     *
     * @param id the id of the biochemicalTest to save.
     * @param biochemicalTest the biochemicalTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated biochemicalTest,
     * or with status {@code 400 (Bad Request)} if the biochemicalTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the biochemicalTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/biochemical-tests/{id}")
    public Mono<ResponseEntity<BiochemicalTest>> updateBiochemicalTest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BiochemicalTest biochemicalTest
    ) throws URISyntaxException {
        log.debug("REST request to update BiochemicalTest : {}, {}", id, biochemicalTest);
        if (biochemicalTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, biochemicalTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return biochemicalTestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return biochemicalTestService
                    .save(biochemicalTest)
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
     * {@code PATCH  /biochemical-tests/:id} : Partial updates given fields of an existing biochemicalTest, field will ignore if it is null
     *
     * @param id the id of the biochemicalTest to save.
     * @param biochemicalTest the biochemicalTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated biochemicalTest,
     * or with status {@code 400 (Bad Request)} if the biochemicalTest is not valid,
     * or with status {@code 404 (Not Found)} if the biochemicalTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the biochemicalTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/biochemical-tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BiochemicalTest>> partialUpdateBiochemicalTest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BiochemicalTest biochemicalTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update BiochemicalTest partially : {}, {}", id, biochemicalTest);
        if (biochemicalTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, biochemicalTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return biochemicalTestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BiochemicalTest> result = biochemicalTestService.partialUpdate(biochemicalTest);

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
     * {@code GET  /biochemical-tests} : get all the biochemicalTests.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of biochemicalTests in body.
     */
    @GetMapping("/biochemical-tests")
    public Mono<ResponseEntity<List<BiochemicalTest>>> getAllBiochemicalTests(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of BiochemicalTests");
        return biochemicalTestService
            .countAll()
            .zipWith(biochemicalTestService.findAll(pageable).collectList())
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
     * {@code GET  /biochemical-tests/:id} : get the "id" biochemicalTest.
     *
     * @param id the id of the biochemicalTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the biochemicalTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/biochemical-tests/{id}")
    public Mono<ResponseEntity<BiochemicalTest>> getBiochemicalTest(@PathVariable String id) {
        log.debug("REST request to get BiochemicalTest : {}", id);
        Mono<BiochemicalTest> biochemicalTest = biochemicalTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(biochemicalTest);
    }

    /**
     * {@code DELETE  /biochemical-tests/:id} : delete the "id" biochemicalTest.
     *
     * @param id the id of the biochemicalTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/biochemical-tests/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBiochemicalTest(@PathVariable String id) {
        log.debug("REST request to delete BiochemicalTest : {}", id);
        return biochemicalTestService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
