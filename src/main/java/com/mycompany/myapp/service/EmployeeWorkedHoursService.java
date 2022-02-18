package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EmployeeWorkedHours;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.repository.EmployeeWorkedHoursRepository;
import com.mycompany.myapp.service.dto.HorasTrabajadasEmpleadoDTO;
import com.mycompany.myapp.web.rest.errors.ExceptionApi;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeWorkedHours}.
 */
@Service
@Transactional
public class EmployeeWorkedHoursService {

    private final Logger log = LoggerFactory.getLogger(EmployeeWorkedHoursService.class);

    private final EmployeeWorkedHoursRepository employeeWorkedHoursRepository;
    private final EmployeeRepository employeeRepository;
    private final JdbcTemplate jdbcTemplate;

    public EmployeeWorkedHoursService(
        EmployeeWorkedHoursRepository employeeWorkedHoursRepository,
        JdbcTemplate jdbcTemplate,
        EmployeeRepository employeeRepository
    ) {
        this.employeeWorkedHoursRepository = employeeWorkedHoursRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.employeeRepository = employeeRepository;
    }

    public Map<String, Object> agregarHorasTrabajadasPorEmpleado(HorasTrabajadasEmpleadoDTO horasTrabajadas) throws ExceptionApi {
        log.warn("Objeto de actualizacion de horas de trabajo recibido: " + horasTrabajadas.toString());
        Map<String, Object> resultado = new HashMap<>();
        // verificar si existe el empleado
        //int cuentaEmpleados = this.jdbcTemplate.queryForObject( "select count(*) from employees e where concat_ws(' ', e.\"name\", e.last_name) = ?;", Integer.class, new Object[]{nombreApellido});
        if (
            horasTrabajadas.getEmployee_id() == null ||
            this.jdbcTemplate.queryForObject(
                    "select count(*) from employees e where e.id = ?;",
                    Integer.class,
                    new Object[] { horasTrabajadas.getEmployee_id() }
                ) <
            1
        ) {
            throw new ExceptionApi("No hay un empleado registrado con ese id", HttpStatus.BAD_REQUEST);
        }
        // verificar que las horas trabajadas no sean mas de 20
        if (horasTrabajadas.getWorked_hours() > 20) {
            throw new ExceptionApi("Ha trabajado mas de 20 horas", HttpStatus.BAD_REQUEST);
        }
        // verificar que la fecha de trabajo sea correcta
        if (horasTrabajadas.getWorked_date().isAfter(LocalDate.now())) {
            throw new ExceptionApi("La fecha de trabajo es incorrecta", HttpStatus.BAD_REQUEST);
        }
        // verificar que el empleado no tennga ya un registro de horas trabajadas
        if (
            this.jdbcTemplate.queryForObject(
                    "select count(*) from employee_worked_hours ewh where ewh.employee_id = ? and ewh.worked_date = ?;",
                    Integer.class,
                    new Object[] { horasTrabajadas.getEmployee_id(), horasTrabajadas.getWorked_date() }
                ) >
            0
        ) {
            throw new ExceptionApi(
                "El empleado ya tiene un registro de horas trabajadas en la fecha proporcionada",
                HttpStatus.BAD_REQUEST
            );
        }
        // agregar las horas trabajadas
        EmployeeWorkedHours employeeWorkedHours = new EmployeeWorkedHours();
        employeeWorkedHours.setEmployee(this.employeeRepository.getById(horasTrabajadas.getEmployee_id()));
        employeeWorkedHours.setWorkedHours(horasTrabajadas.getWorked_hours());
        employeeWorkedHours.setWorkedDate(horasTrabajadas.getWorked_date());
        this.employeeWorkedHoursRepository.save(employeeWorkedHours);

        resultado.put("id", employeeWorkedHours.getId());
        resultado.put("success", true);
        return resultado;
    }

    /**
     * Save a employeeWorkedHours.
     *
     * @param employeeWorkedHours the entity to save.
     * @return the persisted entity.
     */
    public EmployeeWorkedHours save(EmployeeWorkedHours employeeWorkedHours) {
        log.debug("Request to save EmployeeWorkedHours : {}", employeeWorkedHours);
        return employeeWorkedHoursRepository.save(employeeWorkedHours);
    }

    /**
     * Partially update a employeeWorkedHours.
     *
     * @param employeeWorkedHours the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeWorkedHours> partialUpdate(EmployeeWorkedHours employeeWorkedHours) {
        log.debug("Request to partially update EmployeeWorkedHours : {}", employeeWorkedHours);

        return employeeWorkedHoursRepository
            .findById(employeeWorkedHours.getId())
            .map(existingEmployeeWorkedHours -> {
                if (employeeWorkedHours.getWorkedHours() != null) {
                    existingEmployeeWorkedHours.setWorkedHours(employeeWorkedHours.getWorkedHours());
                }
                if (employeeWorkedHours.getWorkedDate() != null) {
                    existingEmployeeWorkedHours.setWorkedDate(employeeWorkedHours.getWorkedDate());
                }

                return existingEmployeeWorkedHours;
            })
            .map(employeeWorkedHoursRepository::save);
    }

    /**
     * Get all the employeeWorkedHours.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeWorkedHours> findAll() {
        log.debug("Request to get all EmployeeWorkedHours");
        return employeeWorkedHoursRepository.findAll();
    }

    /**
     * Get one employeeWorkedHours by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeWorkedHours> findOne(Long id) {
        log.debug("Request to get EmployeeWorkedHours : {}", id);
        return employeeWorkedHoursRepository.findById(id);
    }

    /**
     * Delete the employeeWorkedHours by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeWorkedHours : {}", id);
        employeeWorkedHoursRepository.deleteById(id);
    }
}
