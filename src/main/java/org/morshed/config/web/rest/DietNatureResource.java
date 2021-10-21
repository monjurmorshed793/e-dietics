package org.morshed.config.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.morshed.config.domain.DietNature;
import org.morshed.config.repository.DietNatureRepository;
import org.morshed.config.service.DietNatureService;
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
 * REST controller for managing {@link org.morshed.config.domain.DietNature}.
 */
@RestController
@RequestMapping("/api")
public class DietNatureResource {

    private final Logger log = LoggerFactory.getLogger(DietNatureResource.class);

    private static final String ENTITY_NAME = "dietNature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietNatureService dietNatureService;

    private final DietNatureRepository dietNatureRepository;

    public DietNatureResource(DietNatureService dietNatureService, DietNatureRepository dietNatureRepository) {
        this.dietNatureService = dietNatureService;
        this.dietNatureRepository = dietNatureRepository;
    }

    /**
     * {@code POST  /diet-natures} : Create a new dietNature.
     *
     * @param dietNature the dietNature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietNature, or with status {@code 400 (Bad Request)} if the dietNature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diet-natures")
    public Mono<ResponseEntity<DietNature>> createDietNature(@RequestBody DietNature dietNature) throws URISyntaxException {
        log.debug("REST request to save DietNature : {}", dietNature);
        if (dietNature.getId() != null) {
            throw new BadRequestAlertException("A new dietNature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return dietNatureService
            .save(dietNature)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/diet-natures/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /diet-natures/:id} : Updates an existing dietNature.
     *
     * @param id the id of the dietNature to save.
     * @param dietNature the dietNature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietNature,
     * or with status {@code 400 (Bad Request)} if the dietNature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietNature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diet-natures/{id}")
    public Mono<ResponseEntity<DietNature>> updateDietNature(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DietNature dietNature
    ) throws URISyntaxException {
        log.debug("REST request to update DietNature : {}, {}", id, dietNature);
        if (dietNature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietNature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dietNatureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return dietNatureService
                    .save(dietNature)
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
     * {@code PATCH  /diet-natures/:id} : Partial updates given fields of an existing dietNature, field will ignore if it is null
     *
     * @param id the id of the dietNature to save.
     * @param dietNature the dietNature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietNature,
     * or with status {@code 400 (Bad Request)} if the dietNature is not valid,
     * or with status {@code 404 (Not Found)} if the dietNature is not found,
     * or with status {@code 500 (Internal Server Error)} if the dietNature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/diet-natures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DietNature>> partialUpdateDietNature(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DietNature dietNature
    ) throws URISyntaxException {
        log.debug("REST request to partial update DietNature partially : {}, {}", id, dietNature);
        if (dietNature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietNature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dietNatureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DietNature> result = dietNatureService.partialUpdate(dietNature);

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
     * {@code GET  /diet-natures} : get all the dietNatures.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dietNatures in body.
     */
    @GetMapping("/diet-natures")
    public Mono<ResponseEntity<List<DietNature>>> getAllDietNatures(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of DietNatures");
        return dietNatureService
            .countAll()
            .zipWith(dietNatureService.findAll(pageable).collectList())
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
     * {@code GET  /diet-natures/:id} : get the "id" dietNature.
     *
     * @param id the id of the dietNature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietNature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diet-natures/{id}")
    public Mono<ResponseEntity<DietNature>> getDietNature(@PathVariable String id) {
        log.debug("REST request to get DietNature : {}", id);
        Mono<DietNature> dietNature = dietNatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dietNature);
    }

    /**
     * {@code DELETE  /diet-natures/:id} : delete the "id" dietNature.
     *
     * @param id the id of the dietNature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diet-natures/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDietNature(@PathVariable String id) {
        log.debug("REST request to delete DietNature : {}", id);
        return dietNatureService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
