package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.service.EmployeeService;
import com.mycompany.myapp.service.dto.EmpleadoDTO;
import com.mycompany.myapp.service.dto.FiltroHorasEmpleadoDTO;
import com.mycompany.myapp.service.dto.PuestoTrabajoDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Employee}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeService employeeService;

    private final EmployeeRepository employeeRepository;

    public EmployeeResource(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/nuevo-empleado")
    public ResponseEntity<?> agregarNuevoEmpleado(@RequestBody EmpleadoDTO empleadoDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = this.employeeService.agregarNuevoEmpleado(empleadoDTO);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (ExceptionApi e) {
            result.put("id", null);
            result.put("success", false);
            //result.put("mensaje", e.getMensaje());
            log.error(e.getMensaje(), e);
            return new ResponseEntity<>(result.toString(), e.getHttpStatus());
        } catch (Exception e) {
            result.put("id", null);
            result.put("success", false);
            log.error("Ocurrio un error al insertar el empleado", e);
            return new ResponseEntity<>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/obtener-empleados-por-puesto")
    public ResponseEntity<?> obtenerEmpleadosPorPuesto(@RequestBody PuestoTrabajoDTO puesto) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = this.employeeService.obtenerEmpleadosPorPuesto(puesto);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (ExceptionApi e) {
            result.put("employees", null);
            result.put("success", false);
            log.error(e.getMensaje(), e);
            return new ResponseEntity<>(result.toString(), e.getHttpStatus());
        } catch (Exception e) {
            result.put("employees", null);
            result.put("success", false);
            log.error("Ocurrio un error al buscar los empleados por puesto", e);
            return new ResponseEntity<>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/total-horas-trabajadas-empleado/por-fechas")
    public ResponseEntity<?> obtenerHorasTrabajadasPorFechas(@RequestBody FiltroHorasEmpleadoDTO filtroHoras) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = this.employeeService.obtenerHorasTrabajadasPorFechas(filtroHoras);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (ExceptionApi e) {
            result.put("total_worked_hours", null);
            result.put("success", false);
            log.error(e.getMensaje(), e);
            return new ResponseEntity<>(result.toString(), e.getHttpStatus());
        } catch (Exception e) {
            result.put("total_worked_hours", null);
            result.put("success", false);
            log.error("Ocurrio un error al buscar las horas trabajadas por fecha", e);
            return new ResponseEntity<>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/total-pago-empleado/por-fechas")
    public ResponseEntity<?> obtenerPagoEmpleadoPorFechas(@RequestBody FiltroHorasEmpleadoDTO filtroHoras) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = this.employeeService.obtenerPagoEmpleadoPorFechas(filtroHoras);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (ExceptionApi e) {
            result.put("payment", null);
            result.put("success", false);
            log.error(e.getMensaje(), e);
            return new ResponseEntity<>(result.toString(), e.getHttpStatus());
        } catch (Exception e) {
            result.put("payment", null);
            result.put("success", false);
            log.error("Ocurrio un error al buscar el pago total por fecha", e);
            return new ResponseEntity<>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * {@code POST  /employees} : Create a new employee.
     *
     * @param employee the employee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employee, or with status {@code 400 (Bad Request)} if the employee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employee);
        if (employee.getId() != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employee result = employeeService.save(employee);
        return ResponseEntity
            .created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employees/:id} : Updates an existing employee.
     *
     * @param id the id of the employee to save.
     * @param employee the employee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employee,
     * or with status {@code 400 (Bad Request)} if the employee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Employee employee
    ) throws URISyntaxException {
        log.debug("REST request to update Employee : {}, {}", id, employee);
        if (employee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Employee result = employeeService.save(employee);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employees/:id} : Partial updates given fields of an existing employee, field will ignore if it is null
     *
     * @param id the id of the employee to save.
     * @param employee the employee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employee,
     * or with status {@code 400 (Bad Request)} if the employee is not valid,
     * or with status {@code 404 (Not Found)} if the employee is not found,
     * or with status {@code 500 (Internal Server Error)} if the employee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Employee> partialUpdateEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Employee employee
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employee partially : {}, {}", id, employee);
        if (employee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Employee> result = employeeService.partialUpdate(employee);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.getId().toString())
        );
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        log.debug("REST request to get all Employees");
        return employeeService.findAll();
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @param id the id of the employee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<Employee> employee = employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employee);
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employee.
     *
     * @param id the id of the employee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
