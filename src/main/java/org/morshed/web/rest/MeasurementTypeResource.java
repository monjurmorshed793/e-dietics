package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.morshed.domain.MeasurementType;
import org.morshed.repository.MeasurementTypeRepository;
import org.morshed.service.MeasurementTypeService;
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
 * REST controller for managing {@link org.morshed.domain.MeasurementType}.
 */
@RestController
@RequestMapping("/api")
public class MeasurementTypeResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementTypeResource.class);

    private static final String ENTITY_NAME = "measurementType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasurementTypeService measurementTypeService;

    private final MeasurementTypeRepository measurementTypeRepository;

    public MeasurementTypeResource(MeasurementTypeService measurementTypeService, MeasurementTypeRepository measurementTypeRepository) {
        this.measurementTypeService = measurementTypeService;
        this.measurementTypeRepository = measurementTypeRepository;
    }

    /**
     * {@code POST  /measurement-types} : Create a new measurementType.
     *
     * @param measurementType the measurementType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measurementType, or with status {@code 400 (Bad Request)} if the measurementType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measurement-types")
    public Mono<ResponseEntity<MeasurementType>> createMeasurementType(@Valid @RequestBody MeasurementType measurementType)
        throws URISyntaxException {
        log.debug("REST request to save MeasurementType : {}", measurementType);
        if (measurementType.getId() != null) {
            throw new BadRequestAlertException("A new measurementType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return measurementTypeService
            .save(measurementType)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/measurement-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /measurement-types/:id} : Updates an existing measurementType.
     *
     * @param id the id of the measurementType to save.
     * @param measurementType the measurementType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurementType,
     * or with status {@code 400 (Bad Request)} if the measurementType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measurementType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measurement-types/{id}")
    public Mono<ResponseEntity<MeasurementType>> updateMeasurementType(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody MeasurementType measurementType
    ) throws URISyntaxException {
        log.debug("REST request to update MeasurementType : {}, {}", id, measurementType);
        if (measurementType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measurementType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return measurementTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return measurementTypeService
                    .save(measurementType)
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
     * {@code PATCH  /measurement-types/:id} : Partial updates given fields of an existing measurementType, field will ignore if it is null
     *
     * @param id the id of the measurementType to save.
     * @param measurementType the measurementType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurementType,
     * or with status {@code 400 (Bad Request)} if the measurementType is not valid,
     * or with status {@code 404 (Not Found)} if the measurementType is not found,
     * or with status {@code 500 (Internal Server Error)} if the measurementType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/measurement-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MeasurementType>> partialUpdateMeasurementType(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody MeasurementType measurementType
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeasurementType partially : {}, {}", id, measurementType);
        if (measurementType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measurementType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return measurementTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MeasurementType> result = measurementTypeService.partialUpdate(measurementType);

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
     * {@code GET  /measurement-types} : get all the measurementTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measurementTypes in body.
     */
    @GetMapping("/measurement-types")
    public Mono<ResponseEntity<List<MeasurementType>>> getAllMeasurementTypes(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of MeasurementTypes");
        return measurementTypeService
            .countAll()
            .zipWith(measurementTypeService.findAll(pageable).collectList())
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
     * {@code GET  /measurement-types/:id} : get the "id" measurementType.
     *
     * @param id the id of the measurementType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measurementType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measurement-types/{id}")
    public Mono<ResponseEntity<MeasurementType>> getMeasurementType(@PathVariable String id) {
        log.debug("REST request to get MeasurementType : {}", id);
        Mono<MeasurementType> measurementType = measurementTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measurementType);
    }

    /**
     * {@code DELETE  /measurement-types/:id} : delete the "id" measurementType.
     *
     * @param id the id of the measurementType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measurement-types/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteMeasurementType(@PathVariable String id) {
        log.debug("REST request to delete MeasurementType : {}", id);
        return measurementTypeService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
