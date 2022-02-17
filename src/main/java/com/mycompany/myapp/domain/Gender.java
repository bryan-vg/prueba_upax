package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Gender.
 */
@Entity
@Table(name = "genders")
public class Gender implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "gender")
    @JsonIgnoreProperties(value = { "employeeWorkedHours", "job", "gender" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gender id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Gender name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setGender(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setGender(this));
        }
        this.employees = employees;
    }

    public Gender employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Gender addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setGender(this);
        return this;
    }

    public Gender removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setGender(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gender)) {
            return false;
        }
        return id != null && id.equals(((Gender) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gender{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
