package com.mycompany.myapp.service.dto;

public class PuestoTrabajoDTO {

    private Long job_id;

    public PuestoTrabajoDTO() {}

    public PuestoTrabajoDTO(Long job_id) {
        this.job_id = job_id;
    }

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }

    @Override
    public String toString() {
        return "PuestoTrabajoDTO{" + "job_id=" + job_id + '}';
    }
}
