package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class AppointmentWorkId {

    private Long appointmentId;
    private Long workId;

    public AppointmentWorkId() {}

    public AppointmentWorkId(Long appointmentId, Long workId) {
        this.appointmentId = appointmentId;
        this.workId = workId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentWorkId that = (AppointmentWorkId) o;
        return Objects.equals(appointmentId, that.appointmentId) && Objects.equals(workId, that.workId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId, workId);
    }
}
