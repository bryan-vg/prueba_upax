package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Gender;
import com.mycompany.myapp.repository.GenderRepository;
import com.mycompany.myapp.service.GenderService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Gender}.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

    private final Logger log = LoggerFactory.getLogger(GenderResource.class);

    private static final String ENTITY_NAME = "gender";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenderService genderService;

    private final GenderRepository genderRepository;

    public GenderResource(GenderService genderService, GenderRepository genderRepository) {
        this.genderService = genderService;
        this.genderRepository = genderRepository;
    }

    /**
     * {@code POST  /genders} : Create a new gender.
     *
     * @param gender the gender to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gender, or with status {@code 400 (Bad Request)} if the gender has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/genders")
    public ResponseEntity<Gender> createGender(@RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to save Gender : {}", gender);
        if (gender.getId() != null) {
            throw new BadRequestAlertException("A new gender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gender result = genderService.save(gender);
        return ResponseEntity
            .created(new URI("/api/genders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /genders/:id} : Updates an existing gender.
     *
     * @param id the id of the gender to save.
     * @param gender the gender to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gender,
     * or with status {@code 400 (Bad Request)} if the gender is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gender couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/genders/{id}")
    public ResponseEntity<Gender> updateGender(@PathVariable(value = "id", required = false) final Long id, @RequestBody Gender gender)
        throws URISyntaxException {
        log.debug("REST request to update Gender : {}, {}", id, gender);
        if (gender.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gender.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Gender result = genderService.save(gender);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gender.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /genders/:id} : Partial updates given fields of an existing gender, field will ignore if it is null
     *
     * @param id the id of the gender to save.
     * @param gender the gender to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gender,
     * or with status {@code 400 (Bad Request)} if the gender is not valid,
     * or with status {@code 404 (Not Found)} if the gender is not found,
     * or with status {@code 500 (Internal Server Error)} if the gender couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/genders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Gender> partialUpdateGender(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Gender gender
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gender partially : {}, {}", id, gender);
        if (gender.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gender.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gender> result = genderService.partialUpdate(gender);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gender.getId().toString())
        );
    }

    /**
     * {@code GET  /genders} : get all the genders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genders in body.
     */
    @GetMapping("/genders")
    public List<Gender> getAllGenders() {
        log.debug("REST request to get all Genders");
        return genderService.findAll();
    }

    /**
     * {@code GET  /genders/:id} : get the "id" gender.
     *
     * @param id the id of the gender to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gender, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/genders/{id}")
    public ResponseEntity<Gender> getGender(@PathVariable Long id) {
        log.debug("REST request to get Gender : {}", id);
        Optional<Gender> gender = genderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gender);
    }

    /**
     * {@code DELETE  /genders/:id} : delete the "id" gender.
     *
     * @param id the id of the gender to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/genders/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
        log.debug("REST request to delete Gender : {}", id);
        genderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
