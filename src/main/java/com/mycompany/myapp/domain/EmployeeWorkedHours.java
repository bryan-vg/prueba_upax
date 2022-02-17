package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A EmployeeWorkedHours.
 */
@Entity
@Table(name = "employee_worked_hours")
public class EmployeeWorkedHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "worked_hours")
    private Integer workedHours;

    @Column(name = "worked_date")
    private LocalDate workedDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employeeWorkedHours", "job", "gender" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeWorkedHours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWorkedHours() {
        return this.workedHours;
    }

    public EmployeeWorkedHours workedHours(Integer workedHours) {
        this.setWorkedHours(workedHours);
        return this;
    }

    public void setWorkedHours(Integer workedHours) {
        this.workedHours = workedHours;
    }

    public LocalDate getWorkedDate() {
        return this.workedDate;
    }

    public EmployeeWorkedHours workedDate(LocalDate workedDate) {
        this.setWorkedDate(workedDate);
        return this;
    }

    public void setWorkedDate(LocalDate workedDate) {
        this.workedDate = workedDate;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeWorkedHours employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeWorkedHours)) {
            return false;
        }
        return id != null && id.equals(((EmployeeWorkedHours) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeWorkedHours{" +
            "id=" + getId() +
            ", workedHours=" + getWorkedHours() +
            ", workedDate='" + getWorkedDate() + "'" +
            "}";
    }
}
