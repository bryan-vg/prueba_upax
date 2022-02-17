package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeWorkedHoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeWorkedHours.class);
        EmployeeWorkedHours employeeWorkedHours1 = new EmployeeWorkedHours();
        employeeWorkedHours1.setId(1L);
        EmployeeWorkedHours employeeWorkedHours2 = new EmployeeWorkedHours();
        employeeWorkedHours2.setId(employeeWorkedHours1.getId());
        assertThat(employeeWorkedHours1).isEqualTo(employeeWorkedHours2);
        employeeWorkedHours2.setId(2L);
        assertThat(employeeWorkedHours1).isNotEqualTo(employeeWorkedHours2);
        employeeWorkedHours1.setId(null);
        assertThat(employeeWorkedHours1).isNotEqualTo(employeeWorkedHours2);
    }
}
