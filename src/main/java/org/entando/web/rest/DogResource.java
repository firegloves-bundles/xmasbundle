package org.entando.web.rest;

import org.entando.domain.Dog;
import org.entando.service.DogService;
import org.entando.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.entando.domain.Dog}.
 */
@RestController
@RequestMapping("/api")
public class DogResource {

    private final Logger log = LoggerFactory.getLogger(DogResource.class);

    private static final String ENTITY_NAME = "xmasbundleDog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DogService dogService;

    public DogResource(DogService dogService) {
        this.dogService = dogService;
    }

    /**
     * {@code POST  /dogs} : Create a new dog.
     *
     * @param dog the dog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dog, or with status {@code 400 (Bad Request)} if the dog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dogs")
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) throws URISyntaxException {
        log.debug("REST request to save Dog : {}", dog);
        if (dog.getId() != null) {
            throw new BadRequestAlertException("A new dog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dog result = dogService.save(dog);
        return ResponseEntity.created(new URI("/api/dogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dogs} : Updates an existing dog.
     *
     * @param dog the dog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dog,
     * or with status {@code 400 (Bad Request)} if the dog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dogs")
    public ResponseEntity<Dog> updateDog(@RequestBody Dog dog) throws URISyntaxException {
        log.debug("REST request to update Dog : {}", dog);
        if (dog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dog result = dogService.save(dog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dogs} : get all the dogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dogs in body.
     */
    @GetMapping("/dogs")
    public List<Dog> getAllDogs() {
        log.debug("REST request to get all Dogs");
        return dogService.findAll();
    }

    /**
     * {@code GET  /dogs/:id} : get the "id" dog.
     *
     * @param id the id of the dog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dogs/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        log.debug("REST request to get Dog : {}", id);
        Optional<Dog> dog = dogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dog);
    }

    /**
     * {@code DELETE  /dogs/:id} : delete the "id" dog.
     *
     * @param id the id of the dog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        log.debug("REST request to delete Dog : {}", id);

        dogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
