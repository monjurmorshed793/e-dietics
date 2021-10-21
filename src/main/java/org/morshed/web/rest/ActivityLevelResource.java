package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.morshed.domain.ActivityLevel;
import org.morshed.repository.ActivityLevelRepository;
import org.morshed.service.ActivityLevelService;
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
 * REST controller for managing {@link org.morshed.domain.ActivityLevel}.
 */
@RestController
@RequestMapping("/api")
public class ActivityLevelResource {

    private final Logger log = LoggerFactory.getLogger(ActivityLevelResource.class);

    private static final String ENTITY_NAME = "activityLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityLevelService activityLevelService;

    private final ActivityLevelRepository activityLevelRepository;

    public ActivityLevelResource(ActivityLevelService activityLevelService, ActivityLevelRepository activityLevelRepository) {
        this.activityLevelService = activityLevelService;
        this.activityLevelRepository = activityLevelRepository;
    }

    /**
     * {@code POST  /activity-levels} : Create a new activityLevel.
     *
     * @param activityLevel the activityLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityLevel, or with status {@code 400 (Bad Request)} if the activityLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activity-levels")
    public Mono<ResponseEntity<ActivityLevel>> createActivityLevel(@Valid @RequestBody ActivityLevel activityLevel)
        throws URISyntaxException {
        log.debug("REST request to save ActivityLevel : {}", activityLevel);
        if (activityLevel.getId() != null) {
            throw new BadRequestAlertException("A new activityLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return activityLevelService
            .save(activityLevel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/activity-levels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /activity-levels/:id} : Updates an existing activityLevel.
     *
     * @param id the id of the activityLevel to save.
     * @param activityLevel the activityLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityLevel,
     * or with status {@code 400 (Bad Request)} if the activityLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-levels/{id}")
    public Mono<ResponseEntity<ActivityLevel>> updateActivityLevel(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ActivityLevel activityLevel
    ) throws URISyntaxException {
        log.debug("REST request to update ActivityLevel : {}, {}", id, activityLevel);
        if (activityLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return activityLevelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return activityLevelService
                    .save(activityLevel)
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
     * {@code PATCH  /activity-levels/:id} : Partial updates given fields of an existing activityLevel, field will ignore if it is null
     *
     * @param id the id of the activityLevel to save.
     * @param activityLevel the activityLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityLevel,
     * or with status {@code 400 (Bad Request)} if the activityLevel is not valid,
     * or with status {@code 404 (Not Found)} if the activityLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the activityLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/activity-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ActivityLevel>> partialUpdateActivityLevel(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ActivityLevel activityLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActivityLevel partially : {}, {}", id, activityLevel);
        if (activityLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return activityLevelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ActivityLevel> result = activityLevelService.partialUpdate(activityLevel);

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
     * {@code GET  /activity-levels} : get all the activityLevels.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityLevels in body.
     */
    @GetMapping("/activity-levels")
    public Mono<ResponseEntity<List<ActivityLevel>>> getAllActivityLevels(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ActivityLevels");
        return activityLevelService
            .countAll()
            .zipWith(activityLevelService.findAll(pageable).collectList())
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
     * {@code GET  /activity-levels/:id} : get the "id" activityLevel.
     *
     * @param id the id of the activityLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-levels/{id}")
    public Mono<ResponseEntity<ActivityLevel>> getActivityLevel(@PathVariable String id) {
        log.debug("REST request to get ActivityLevel : {}", id);
        Mono<ActivityLevel> activityLevel = activityLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityLevel);
    }

    /**
     * {@code DELETE  /activity-levels/:id} : delete the "id" activityLevel.
     *
     * @param id the id of the activityLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activity-levels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteActivityLevel(@PathVariable String id) {
        log.debug("REST request to delete ActivityLevel : {}", id);
        return activityLevelService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
