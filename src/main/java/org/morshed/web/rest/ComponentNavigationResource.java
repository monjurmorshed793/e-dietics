package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.morshed.domain.ComponentNavigation;
import org.morshed.repository.ComponentNavigationRepository;
import org.morshed.service.ComponentNavigationService;
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
 * REST controller for managing {@link org.morshed.domain.ComponentNavigation}.
 */
@RestController
@RequestMapping("/api")
public class ComponentNavigationResource {

    private final Logger log = LoggerFactory.getLogger(ComponentNavigationResource.class);

    private static final String ENTITY_NAME = "componentNavigation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponentNavigationService componentNavigationService;

    private final ComponentNavigationRepository componentNavigationRepository;

    public ComponentNavigationResource(
        ComponentNavigationService componentNavigationService,
        ComponentNavigationRepository componentNavigationRepository
    ) {
        this.componentNavigationService = componentNavigationService;
        this.componentNavigationRepository = componentNavigationRepository;
    }

    /**
     * {@code POST  /component-navigations} : Create a new componentNavigation.
     *
     * @param componentNavigation the componentNavigation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componentNavigation, or with status {@code 400 (Bad Request)} if the componentNavigation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/component-navigations")
    public Mono<ResponseEntity<ComponentNavigation>> createComponentNavigation(@Valid @RequestBody ComponentNavigation componentNavigation)
        throws URISyntaxException {
        log.debug("REST request to save ComponentNavigation : {}", componentNavigation);
        if (componentNavigation.getId() != null) {
            throw new BadRequestAlertException("A new componentNavigation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return componentNavigationService
            .save(componentNavigation)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/component-navigations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /component-navigations/:id} : Updates an existing componentNavigation.
     *
     * @param id the id of the componentNavigation to save.
     * @param componentNavigation the componentNavigation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentNavigation,
     * or with status {@code 400 (Bad Request)} if the componentNavigation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componentNavigation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/component-navigations/{id}")
    public Mono<ResponseEntity<ComponentNavigation>> updateComponentNavigation(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ComponentNavigation componentNavigation
    ) throws URISyntaxException {
        log.debug("REST request to update ComponentNavigation : {}, {}", id, componentNavigation);
        if (componentNavigation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentNavigation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return componentNavigationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return componentNavigationService
                    .save(componentNavigation)
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
     * {@code PATCH  /component-navigations/:id} : Partial updates given fields of an existing componentNavigation, field will ignore if it is null
     *
     * @param id the id of the componentNavigation to save.
     * @param componentNavigation the componentNavigation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentNavigation,
     * or with status {@code 400 (Bad Request)} if the componentNavigation is not valid,
     * or with status {@code 404 (Not Found)} if the componentNavigation is not found,
     * or with status {@code 500 (Internal Server Error)} if the componentNavigation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/component-navigations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ComponentNavigation>> partialUpdateComponentNavigation(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ComponentNavigation componentNavigation
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComponentNavigation partially : {}, {}", id, componentNavigation);
        if (componentNavigation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentNavigation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return componentNavigationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ComponentNavigation> result = componentNavigationService.partialUpdate(componentNavigation);

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
     * {@code GET  /component-navigations} : get all the componentNavigations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componentNavigations in body.
     */
    @GetMapping("/component-navigations")
    public Mono<ResponseEntity<List<ComponentNavigation>>> getAllComponentNavigations(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ComponentNavigations");
        return componentNavigationService
            .countAll()
            .zipWith(componentNavigationService.findAll(pageable).collectList())
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
     * {@code GET  /component-navigations/:id} : get the "id" componentNavigation.
     *
     * @param id the id of the componentNavigation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componentNavigation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/component-navigations/{id}")
    public Mono<ResponseEntity<ComponentNavigation>> getComponentNavigation(@PathVariable String id) {
        log.debug("REST request to get ComponentNavigation : {}", id);
        Mono<ComponentNavigation> componentNavigation = componentNavigationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(componentNavigation);
    }

    /**
     * {@code DELETE  /component-navigations/:id} : delete the "id" componentNavigation.
     *
     * @param id the id of the componentNavigation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/component-navigations/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteComponentNavigation(@PathVariable String id) {
        log.debug("REST request to delete ComponentNavigation : {}", id);
        return componentNavigationService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
