package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public class HorasTrabajadasEmpleadoDTO {

    private Long employee_id;
    private Integer worked_hours;
    private LocalDate worked_date;

    public HorasTrabajadasEmpleadoDTO() {}

    public HorasTrabajadasEmpleadoDTO(Long employee_id, Integer worked_hours, LocalDate worked_date) {
        this.employee_id = employee_id;
        this.worked_hours = worked_hours;
        this.worked_date = worked_date;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Integer getWorked_hours() {
        return worked_hours;
    }

    public void setWorked_hours(Integer worked_hours) {
        this.worked_hours = worked_hours;
    }

    public LocalDate getWorked_date() {
        return worked_date;
    }

    public void setWorked_date(LocalDate worked_date) {
        this.worked_date = worked_date;
    }

    @Override
    public String toString() {
        return (
            "HorasTrabajadasEmpleadoDTO{" +
            "employee_id=" +
            employee_id +
            ", worked_hours=" +
            worked_hours +
            ", worked_date=" +
            worked_date +
            '}'
        );
    }
}
