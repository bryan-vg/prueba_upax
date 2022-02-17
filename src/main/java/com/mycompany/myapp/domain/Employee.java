package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Employee.
 */
@Entity
@Table(name = "employees")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<EmployeeWorkedHours> employeeWorkedHours = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Job job;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Gender gender;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Employee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Employee birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<EmployeeWorkedHours> getEmployeeWorkedHours() {
        return this.employeeWorkedHours;
    }

    public void setEmployeeWorkedHours(Set<EmployeeWorkedHours> employeeWorkedHours) {
        if (this.employeeWorkedHours != null) {
            this.employeeWorkedHours.forEach(i -> i.setEmployee(null));
        }
        if (employeeWorkedHours != null) {
            employeeWorkedHours.forEach(i -> i.setEmployee(this));
        }
        this.employeeWorkedHours = employeeWorkedHours;
    }

    public Employee employeeWorkedHours(Set<EmployeeWorkedHours> employeeWorkedHours) {
        this.setEmployeeWorkedHours(employeeWorkedHours);
        return this;
    }

    public Employee addEmployeeWorkedHours(EmployeeWorkedHours employeeWorkedHours) {
        this.employeeWorkedHours.add(employeeWorkedHours);
        employeeWorkedHours.setEmployee(this);
        return this;
    }

    public Employee removeEmployeeWorkedHours(EmployeeWorkedHours employeeWorkedHours) {
        this.employeeWorkedHours.remove(employeeWorkedHours);
        employeeWorkedHours.setEmployee(null);
        return this;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Employee job(Job job) {
        this.setJob(job);
        return this;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
