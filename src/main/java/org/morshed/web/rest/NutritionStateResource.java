package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.morshed.domain.NutritionState;
import org.morshed.repository.NutritionStateRepository;
import org.morshed.service.NutritionStateService;
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
 * REST controller for managing {@link org.morshed.domain.NutritionState}.
 */
@RestController
@RequestMapping("/api")
public class NutritionStateResource {

    private final Logger log = LoggerFactory.getLogger(NutritionStateResource.class);

    private static final String ENTITY_NAME = "nutritionState";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NutritionStateService nutritionStateService;

    private final NutritionStateRepository nutritionStateRepository;

    public NutritionStateResource(NutritionStateService nutritionStateService, NutritionStateRepository nutritionStateRepository) {
        this.nutritionStateService = nutritionStateService;
        this.nutritionStateRepository = nutritionStateRepository;
    }

    /**
     * {@code POST  /nutrition-states} : Create a new nutritionState.
     *
     * @param nutritionState the nutritionState to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nutritionState, or with status {@code 400 (Bad Request)} if the nutritionState has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nutrition-states")
    public Mono<ResponseEntity<NutritionState>> createNutritionState(@Valid @RequestBody NutritionState nutritionState)
        throws URISyntaxException {
        log.debug("REST request to save NutritionState : {}", nutritionState);
        if (nutritionState.getId() != null) {
            throw new BadRequestAlertException("A new nutritionState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return nutritionStateService
            .save(nutritionState)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/nutrition-states/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /nutrition-states/:id} : Updates an existing nutritionState.
     *
     * @param id the id of the nutritionState to save.
     * @param nutritionState the nutritionState to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nutritionState,
     * or with status {@code 400 (Bad Request)} if the nutritionState is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nutritionState couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nutrition-states/{id}")
    public Mono<ResponseEntity<NutritionState>> updateNutritionState(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody NutritionState nutritionState
    ) throws URISyntaxException {
        log.debug("REST request to update NutritionState : {}, {}", id, nutritionState);
        if (nutritionState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nutritionState.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nutritionStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return nutritionStateService
                    .save(nutritionState)
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
     * {@code PATCH  /nutrition-states/:id} : Partial updates given fields of an existing nutritionState, field will ignore if it is null
     *
     * @param id the id of the nutritionState to save.
     * @param nutritionState the nutritionState to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nutritionState,
     * or with status {@code 400 (Bad Request)} if the nutritionState is not valid,
     * or with status {@code 404 (Not Found)} if the nutritionState is not found,
     * or with status {@code 500 (Internal Server Error)} if the nutritionState couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nutrition-states/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NutritionState>> partialUpdateNutritionState(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody NutritionState nutritionState
    ) throws URISyntaxException {
        log.debug("REST request to partial update NutritionState partially : {}, {}", id, nutritionState);
        if (nutritionState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nutritionState.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nutritionStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NutritionState> result = nutritionStateService.partialUpdate(nutritionState);

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
     * {@code GET  /nutrition-states} : get all the nutritionStates.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nutritionStates in body.
     */
    @GetMapping("/nutrition-states")
    public Mono<ResponseEntity<List<NutritionState>>> getAllNutritionStates(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of NutritionStates");
        return nutritionStateService
            .countAll()
            .zipWith(nutritionStateService.findAll(pageable).collectList())
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
     * {@code GET  /nutrition-states/:id} : get the "id" nutritionState.
     *
     * @param id the id of the nutritionState to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nutritionState, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nutrition-states/{id}")
    public Mono<ResponseEntity<NutritionState>> getNutritionState(@PathVariable String id) {
        log.debug("REST request to get NutritionState : {}", id);
        Mono<NutritionState> nutritionState = nutritionStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nutritionState);
    }

    /**
     * {@code DELETE  /nutrition-states/:id} : delete the "id" nutritionState.
     *
     * @param id the id of the nutritionState to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nutrition-states/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteNutritionState(@PathVariable String id) {
        log.debug("REST request to delete NutritionState : {}", id);
        return nutritionStateService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
