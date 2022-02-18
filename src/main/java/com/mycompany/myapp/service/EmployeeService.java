package com.mycompany.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.repository.GenderRepository;
import com.mycompany.myapp.repository.JobRepository;
import com.mycompany.myapp.service.dto.EmpleadoDTO;
import com.mycompany.myapp.service.dto.FiltroHorasEmpleadoDTO;
import com.mycompany.myapp.service.dto.PuestoTrabajoDTO;
import com.mycompany.myapp.web.rest.errors.ExceptionApi;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final GenderRepository genderRepository;
    private final JobRepository jobRepository;
    private final JdbcTemplate jdbcTemplate;

    public EmployeeService(
        EmployeeRepository employeeRepository,
        JdbcTemplate jdbcTemplate,
        GenderRepository genderRepository,
        JobRepository jobRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.genderRepository = genderRepository;
        this.jobRepository = jobRepository;
    }

    public Map<String, Object> agregarNuevoEmpleado(EmpleadoDTO empleadoDTO) throws ExceptionApi {
        Map<String, Object> resultado = new HashMap<>();
        // verificar si ya esta el nombre registrado
        String nombre = String.valueOf(stringNoEstaVacio(empleadoDTO.getName()) ? empleadoDTO.getName() : "").concat(" ");
        String apellido = String.valueOf(stringNoEstaVacio(empleadoDTO.getLast_name()) ? empleadoDTO.getLast_name() : "");
        String nombreApellido = String.valueOf(nombre.concat(apellido)).trim();

        int cuentaEmpleados =
            this.jdbcTemplate.queryForObject(
                    "select count(*) from employees e where concat_ws(' ', e.\"name\", e.last_name) = ?;",
                    Integer.class,
                    new Object[] { nombreApellido }
                );

        if (cuentaEmpleados > 0) {
            throw new ExceptionApi("Ya hay una persona registrada con el nombre " + nombreApellido, HttpStatus.BAD_REQUEST);
        }

        //verificar que sea mayor de edad
        if (empleadoDTO.getBirthdate().until(LocalDate.now(), ChronoUnit.YEARS) < 18) {
            throw new ExceptionApi("No cumple con la edad requerida", HttpStatus.BAD_REQUEST);
        }

        // verificar que existan el genero y puesto
        if (
            empleadoDTO.getGender_id() == null ||
            (
                empleadoDTO.getGender_id() != null &&
                this.jdbcTemplate.queryForObject(
                        "select count(*) from genders g where g.id = ?;",
                        Integer.class,
                        new Object[] { empleadoDTO.getGender_id() }
                    ) <
                1
            )
        ) {
            throw new ExceptionApi("No se existe un genero con ese id", HttpStatus.BAD_REQUEST);
        }

        if (
            empleadoDTO.getJob_id() == null ||
            (
                empleadoDTO.getJob_id() != null &&
                this.jdbcTemplate.queryForObject(
                        "select count(*) from jobs j where j.id = ?;",
                        Integer.class,
                        new Object[] { empleadoDTO.getJob_id() }
                    ) <
                1
            )
        ) {
            throw new ExceptionApi("No se existe un puesto con ese id", HttpStatus.BAD_REQUEST);
        }
        // proceder a guardar el empleado
        Employee employee = new Employee();
        employee.setBirthDate(empleadoDTO.getBirthdate());
        employee.setName(empleadoDTO.getName());
        employee.setLastName(empleadoDTO.getLast_name());
        employee.setGender(this.genderRepository.getById(empleadoDTO.getGender_id()));
        employee.setJob(this.jobRepository.getById(empleadoDTO.getJob_id()));
        this.employeeRepository.save(employee);

        resultado.put("id", employee.getId());
        resultado.put("success", true);
        return resultado;
    }

    public Map<String, Object> obtenerEmpleadosPorPuesto(PuestoTrabajoDTO puestoTrabajoDTO) throws ExceptionApi {
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> listaFinal = new ArrayList<>();
        // verificar si existe el puesto
        if (
            puestoTrabajoDTO.getJob_id() == null ||
            (
                puestoTrabajoDTO.getJob_id() != null &&
                this.jdbcTemplate.queryForObject(
                        "select count(*) from jobs j where j.id = ?;",
                        Integer.class,
                        new Object[] { puestoTrabajoDTO.getJob_id() }
                    ) <
                1
            )
        ) {
            throw new ExceptionApi("No se existe un puesto con ese id", HttpStatus.BAD_REQUEST);
        }

        // sacar los empleados segun el puesto que se mando en el request:
        List<Map<String, Object>> listaMapsEmpleados =
            this.jdbcTemplate.queryForList(
                    "select row_to_json(t) as emp from (\n" +
                    "select e2.id, e2.\"name\", e2.last_name,\n" +
                    "\tto_char(e2.birth_date, 'dd-mm-YYYY') as birthdate, \n" +
                    "\t( select row_to_json(d) from(\n" +
                    "\t\tselect j.id, j.\"name\", j.salary from jobs j where j.id in (\n" +
                    "\t\t\tselect e.job_id from employees e where e.job_id = ? and e.id = e2.id) ) d\n" +
                    "\t) as job,\n" +
                    "\t( select row_to_json(c) from(\n" +
                    "\t\tselect g.id, g.\"name\" from genders g where g.id in (\n" +
                    "\t\t\tselect em.gender_id from employees em where em.job_id = ? and em.id = e2.id) ) c\n" +
                    "\t) as gender\n" +
                    "\tfrom employees e2 where e2.job_id = ?\n" +
                    ")  t;",
                    new Object[] { puestoTrabajoDTO.getJob_id(), puestoTrabajoDTO.getJob_id(), puestoTrabajoDTO.getJob_id() }
                );

        ObjectMapper objectMapper = new ObjectMapper();
        listaMapsEmpleados.forEach(m -> {
            listaFinal.add(objectMapper.convertValue(m, Map.class));
        });
        resultado.put("employees", listaFinal);
        resultado.put("success", true);
        return resultado;
    }

    public Map<String, Object> obtenerHorasTrabajadasPorFechas(FiltroHorasEmpleadoDTO filtroHoras) throws ExceptionApi {
        Map<String, Object> resultado = new HashMap<>();
        // verificar si existe el empleado
        if (
            filtroHoras.getEmployee_id() == null ||
            (
                filtroHoras.getEmployee_id() != null &&
                this.jdbcTemplate.queryForObject(
                        "select count(*) from employees e where e.id = ?;",
                        Integer.class,
                        new Object[] { filtroHoras.getEmployee_id() }
                    ) <
                1
            )
        ) {
            throw new ExceptionApi("No se existe un empleado con ese id", HttpStatus.BAD_REQUEST);
        }
        // verificar la validez de las fechas
        if (!filtroHoras.getStart_date().isBefore(filtroHoras.getEnd_date())) {
            throw new ExceptionApi("Las fecha de inicio no es anterior a la fecha final", HttpStatus.BAD_REQUEST);
        }
        // se verifica que no se pase de la fecha actual por que postgres marca error si es asi
        if (filtroHoras.getStart_date().isAfter(LocalDate.now()) || filtroHoras.getEnd_date().isAfter(LocalDate.now())) {
            throw new ExceptionApi("Una de las fechas dadas es despues de la fecha actual", HttpStatus.BAD_REQUEST);
        }
        // sacar el total de horas trabajadas del empleado
        int horasTrabajadas =
            this.jdbcTemplate.queryForObject(
                    "select coalesce(sum(ewh.worked_hours), 0) as horas from employee_worked_hours ewh \n" +
                    "where ewh.employee_id = ? and ewh.worked_date between ? and ?;",
                    Integer.class,
                    new Object[] { filtroHoras.getEmployee_id(), filtroHoras.getStart_date(), filtroHoras.getEnd_date() }
                );
        resultado.put("total_worked_hours", horasTrabajadas);
        resultado.put("success", true);
        return resultado;
    }

    public Map<String, Object> obtenerPagoEmpleadoPorFechas(FiltroHorasEmpleadoDTO filtroHoras) throws ExceptionApi {
        Map<String, Object> resultado = new HashMap<>();
        // verificar si existe el empleado
        if (
            filtroHoras.getEmployee_id() == null ||
            (
                filtroHoras.getEmployee_id() != null &&
                this.jdbcTemplate.queryForObject(
                        "select count(*) from employees e where e.id = ?;",
                        Integer.class,
                        new Object[] { filtroHoras.getEmployee_id() }
                    ) <
                1
            )
        ) {
            throw new ExceptionApi("No se existe un empleado con ese id", HttpStatus.BAD_REQUEST);
        }
        // verificar la validez de las fechas
        if (!filtroHoras.getStart_date().isBefore(filtroHoras.getEnd_date())) {
            throw new ExceptionApi("Las fecha de inicio no es anterior a la fecha final", HttpStatus.BAD_REQUEST);
        }
        // se verifica que no se pase de la fecha actual por que postgres marca error si es asi
        if (filtroHoras.getStart_date().isAfter(LocalDate.now()) || filtroHoras.getEnd_date().isAfter(LocalDate.now())) {
            throw new ExceptionApi("Una de las fechas dadas es despues de la fecha actual", HttpStatus.BAD_REQUEST);
        }
        // sacar el total de pago del empleado en el rango de fechas
        BigDecimal pago =
            this.jdbcTemplate.queryForObject(
                    "select coalesce(sum(j.salary), 0) as pago from employee_worked_hours ewh \n" +
                    "inner join employees e on ewh.employee_id = e.id \n" +
                    "inner join jobs j on e.job_id = j.id \n" +
                    "where ewh.employee_id = ? and ewh.worked_date between ? and ?;",
                    BigDecimal.class,
                    new Object[] { filtroHoras.getEmployee_id(), filtroHoras.getStart_date(), filtroHoras.getEnd_date() }
                );
        resultado.put("payment", pago);
        resultado.put("success", true);
        return resultado;
    }

    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Partially update a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getName() != null) {
                    existingEmployee.setName(employee.getName());
                }
                if (employee.getLastName() != null) {
                    existingEmployee.setLastName(employee.getLastName());
                }
                if (employee.getBirthDate() != null) {
                    existingEmployee.setBirthDate(employee.getBirthDate());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    /**
     * Get all the employees.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll();
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }

    public boolean stringNoEstaVacio(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
