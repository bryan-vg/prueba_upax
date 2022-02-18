package com.mycompany.myapp.service.dto;

import java.math.BigDecimal;

public class JobDTO {

    private Long id;
    private String name;
    private BigDecimal salary;

    public JobDTO() {}

    public JobDTO(Long id, String name, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "JobDTO{" + "id=" + id + ", name='" + name + '\'' + ", salary=" + salary + '}';
    }
}
