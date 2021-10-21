package org.morshed.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.morshed.domain.FoodCategory;
import org.morshed.repository.FoodCategoryRepository;
import org.morshed.service.FoodCategoryService;
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
 * REST controller for managing {@link org.morshed.domain.FoodCategory}.
 */
@RestController
@RequestMapping("/api")
public class FoodCategoryResource {

    private final Logger log = LoggerFactory.getLogger(FoodCategoryResource.class);

    private static final String ENTITY_NAME = "foodCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodCategoryService foodCategoryService;

    private final FoodCategoryRepository foodCategoryRepository;

    public FoodCategoryResource(FoodCategoryService foodCategoryService, FoodCategoryRepository foodCategoryRepository) {
        this.foodCategoryService = foodCategoryService;
        this.foodCategoryRepository = foodCategoryRepository;
    }

    /**
     * {@code POST  /food-categories} : Create a new foodCategory.
     *
     * @param foodCategory the foodCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodCategory, or with status {@code 400 (Bad Request)} if the foodCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/food-categories")
    public Mono<ResponseEntity<FoodCategory>> createFoodCategory(@RequestBody FoodCategory foodCategory) throws URISyntaxException {
        log.debug("REST request to save FoodCategory : {}", foodCategory);
        if (foodCategory.getId() != null) {
            throw new BadRequestAlertException("A new foodCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return foodCategoryService
            .save(foodCategory)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/food-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /food-categories/:id} : Updates an existing foodCategory.
     *
     * @param id the id of the foodCategory to save.
     * @param foodCategory the foodCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodCategory,
     * or with status {@code 400 (Bad Request)} if the foodCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/food-categories/{id}")
    public Mono<ResponseEntity<FoodCategory>> updateFoodCategory(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FoodCategory foodCategory
    ) throws URISyntaxException {
        log.debug("REST request to update FoodCategory : {}, {}", id, foodCategory);
        if (foodCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return foodCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return foodCategoryService
                    .save(foodCategory)
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
     * {@code PATCH  /food-categories/:id} : Partial updates given fields of an existing foodCategory, field will ignore if it is null
     *
     * @param id the id of the foodCategory to save.
     * @param foodCategory the foodCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodCategory,
     * or with status {@code 400 (Bad Request)} if the foodCategory is not valid,
     * or with status {@code 404 (Not Found)} if the foodCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/food-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FoodCategory>> partialUpdateFoodCategory(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FoodCategory foodCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodCategory partially : {}, {}", id, foodCategory);
        if (foodCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return foodCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FoodCategory> result = foodCategoryService.partialUpdate(foodCategory);

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
     * {@code GET  /food-categories} : get all the foodCategories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodCategories in body.
     */
    @GetMapping("/food-categories")
    public Mono<ResponseEntity<List<FoodCategory>>> getAllFoodCategories(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of FoodCategories");
        return foodCategoryService
            .countAll()
            .zipWith(foodCategoryService.findAll(pageable).collectList())
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
     * {@code GET  /food-categories/:id} : get the "id" foodCategory.
     *
     * @param id the id of the foodCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/food-categories/{id}")
    public Mono<ResponseEntity<FoodCategory>> getFoodCategory(@PathVariable String id) {
        log.debug("REST request to get FoodCategory : {}", id);
        Mono<FoodCategory> foodCategory = foodCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodCategory);
    }

    /**
     * {@code DELETE  /food-categories/:id} : delete the "id" foodCategory.
     *
     * @param id the id of the foodCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/food-categories/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFoodCategory(@PathVariable String id) {
        log.debug("REST request to delete FoodCategory : {}", id);
        return foodCategoryService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
