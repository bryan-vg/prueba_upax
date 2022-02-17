package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EmployeeWorkedHours;
import com.mycompany.myapp.repository.EmployeeWorkedHoursRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public EmployeeWorkedHoursService(EmployeeWorkedHoursRepository employeeWorkedHoursRepository) {
        this.employeeWorkedHoursRepository = employeeWorkedHoursRepository;
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
