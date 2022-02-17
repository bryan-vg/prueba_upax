package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmployeeWorkedHours;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EmployeeWorkedHours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeWorkedHoursRepository extends JpaRepository<EmployeeWorkedHours, Long> {}
