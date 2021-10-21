package org.morshed.config.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.morshed.config.domain.Supplements;
import org.morshed.config.repository.SupplementsRepository;
import org.morshed.config.service.SupplementsService;
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
 * REST controller for managing {@link org.morshed.config.domain.Supplements}.
 */
@RestController
@RequestMapping("/api")
public class SupplementsResource {

    private final Logger log = LoggerFactory.getLogger(SupplementsResource.class);

    private static final String ENTITY_NAME = "supplements";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplementsService supplementsService;

    private final SupplementsRepository supplementsRepository;

    public SupplementsResource(SupplementsService supplementsService, SupplementsRepository supplementsRepository) {
        this.supplementsService = supplementsService;
        this.supplementsRepository = supplementsRepository;
    }

    /**
     * {@code POST  /supplements} : Create a new supplements.
     *
     * @param supplements the supplements to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplements, or with status {@code 400 (Bad Request)} if the supplements has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplements")
    public Mono<ResponseEntity<Supplements>> createSupplements(@RequestBody Supplements supplements) throws URISyntaxException {
        log.debug("REST request to save Supplements : {}", supplements);
        if (supplements.getId() != null) {
            throw new BadRequestAlertException("A new supplements cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return supplementsService
            .save(supplements)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/supplements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /supplements/:id} : Updates an existing supplements.
     *
     * @param id the id of the supplements to save.
     * @param supplements the supplements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplements,
     * or with status {@code 400 (Bad Request)} if the supplements is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplements/{id}")
    public Mono<ResponseEntity<Supplements>> updateSupplements(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Supplements supplements
    ) throws URISyntaxException {
        log.debug("REST request to update Supplements : {}, {}", id, supplements);
        if (supplements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplementsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return supplementsService
                    .save(supplements)
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
     * {@code PATCH  /supplements/:id} : Partial updates given fields of an existing supplements, field will ignore if it is null
     *
     * @param id the id of the supplements to save.
     * @param supplements the supplements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplements,
     * or with status {@code 400 (Bad Request)} if the supplements is not valid,
     * or with status {@code 404 (Not Found)} if the supplements is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/supplements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Supplements>> partialUpdateSupplements(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Supplements supplements
    ) throws URISyntaxException {
        log.debug("REST request to partial update Supplements partially : {}, {}", id, supplements);
        if (supplements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplementsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Supplements> result = supplementsService.partialUpdate(supplements);

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
     * {@code GET  /supplements} : get all the supplements.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplements in body.
     */
    @GetMapping("/supplements")
    public Mono<ResponseEntity<List<Supplements>>> getAllSupplements(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Supplements");
        return supplementsService
            .countAll()
            .zipWith(supplementsService.findAll(pageable).collectList())
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
     * {@code GET  /supplements/:id} : get the "id" supplements.
     *
     * @param id the id of the supplements to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplements, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplements/{id}")
    public Mono<ResponseEntity<Supplements>> getSupplements(@PathVariable String id) {
        log.debug("REST request to get Supplements : {}", id);
        Mono<Supplements> supplements = supplementsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplements);
    }

    /**
     * {@code DELETE  /supplements/:id} : delete the "id" supplements.
     *
     * @param id the id of the supplements to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplements/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSupplements(@PathVariable String id) {
        log.debug("REST request to delete Supplements : {}", id);
        return supplementsService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
