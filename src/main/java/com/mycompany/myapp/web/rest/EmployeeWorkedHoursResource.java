package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EmployeeWorkedHours;
import com.mycompany.myapp.repository.EmployeeWorkedHoursRepository;
import com.mycompany.myapp.service.EmployeeWorkedHoursService;
import com.mycompany.myapp.service.dto.HorasTrabajadasEmpleadoDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ExceptionApi;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.EmployeeWorkedHours}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeWorkedHoursResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeWorkedHoursResource.class);

    private static final String ENTITY_NAME = "employeeWorkedHours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeWorkedHoursService employeeWorkedHoursService;
    private final EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

    public EmployeeWorkedHoursResource(
        EmployeeWorkedHoursService employeeWorkedHoursService,
        EmployeeWorkedHoursRepository employeeWorkedHoursRepository
    ) {
        this.employeeWorkedHoursService = employeeWorkedHoursService;
        this.employeeWorkedHoursRepository = employeeWorkedHoursRepository;
    }

    @PostMapping("/agregar-horas-trabajadas/empleado")
    public ResponseEntity<?> agregarHorasTrabajadasPorEmpleado(@RequestBody HorasTrabajadasEmpleadoDTO horasTrabajadas) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = this.employeeWorkedHoursService.agregarHorasTrabajadasPorEmpleado(horasTrabajadas);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (ExceptionApi e) {
            result.put("id", null);
            result.put("success", false);
            log.error(e.getMensaje(), e);
            return new ResponseEntity<>(result.toString(), e.getHttpStatus());
        } catch (Exception e) {
            result.put("id", null);
            result.put("success", false);
            log.error("Ocurrio un error al registrar las horas trabajadas del empleado", e);
            return new ResponseEntity<>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * {@code POST  /employee-worked-hours} : Create a new employeeWorkedHours.
     *
     * @param employeeWorkedHours the employeeWorkedHours to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeWorkedHours, or with status {@code 400 (Bad Request)} if the employeeWorkedHours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-worked-hours")
    public ResponseEntity<EmployeeWorkedHours> createEmployeeWorkedHours(@RequestBody EmployeeWorkedHours employeeWorkedHours)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeWorkedHours : {}", employeeWorkedHours);
        if (employeeWorkedHours.getId() != null) {
            throw new BadRequestAlertException("A new employeeWorkedHours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeWorkedHours result = employeeWorkedHoursService.save(employeeWorkedHours);
        return ResponseEntity
            .created(new URI("/api/employee-worked-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-worked-hours/:id} : Updates an existing employeeWorkedHours.
     *
     * @param id the id of the employeeWorkedHours to save.
     * @param employeeWorkedHours the employeeWorkedHours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeWorkedHours,
     * or with status {@code 400 (Bad Request)} if the employeeWorkedHours is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeWorkedHours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-worked-hours/{id}")
    public ResponseEntity<EmployeeWorkedHours> updateEmployeeWorkedHours(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeeWorkedHours employeeWorkedHours
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeWorkedHours : {}, {}", id, employeeWorkedHours);
        if (employeeWorkedHours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeWorkedHours.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeWorkedHoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmployeeWorkedHours result = employeeWorkedHoursService.save(employeeWorkedHours);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeWorkedHours.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employee-worked-hours/:id} : Partial updates given fields of an existing employeeWorkedHours, field will ignore if it is null
     *
     * @param id the id of the employeeWorkedHours to save.
     * @param employeeWorkedHours the employeeWorkedHours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeWorkedHours,
     * or with status {@code 400 (Bad Request)} if the employeeWorkedHours is not valid,
     * or with status {@code 404 (Not Found)} if the employeeWorkedHours is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeWorkedHours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employee-worked-hours/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeWorkedHours> partialUpdateEmployeeWorkedHours(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeeWorkedHours employeeWorkedHours
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeWorkedHours partially : {}, {}", id, employeeWorkedHours);
        if (employeeWorkedHours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeWorkedHours.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeWorkedHoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeWorkedHours> result = employeeWorkedHoursService.partialUpdate(employeeWorkedHours);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeWorkedHours.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-worked-hours} : get all the employeeWorkedHours.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeWorkedHours in body.
     */
    @GetMapping("/employee-worked-hours")
    public List<EmployeeWorkedHours> getAllEmployeeWorkedHours() {
        log.debug("REST request to get all EmployeeWorkedHours");
        return employeeWorkedHoursService.findAll();
    }

    /**
     * {@code GET  /employee-worked-hours/:id} : get the "id" employeeWorkedHours.
     *
     * @param id the id of the employeeWorkedHours to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeWorkedHours, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-worked-hours/{id}")
    public ResponseEntity<EmployeeWorkedHours> getEmployeeWorkedHours(@PathVariable Long id) {
        log.debug("REST request to get EmployeeWorkedHours : {}", id);
        Optional<EmployeeWorkedHours> employeeWorkedHours = employeeWorkedHoursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeWorkedHours);
    }

    /**
     * {@code DELETE  /employee-worked-hours/:id} : delete the "id" employeeWorkedHours.
     *
     * @param id the id of the employeeWorkedHours to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-worked-hours/{id}")
    public ResponseEntity<Void> deleteEmployeeWorkedHours(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeWorkedHours : {}", id);
        employeeWorkedHoursService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
