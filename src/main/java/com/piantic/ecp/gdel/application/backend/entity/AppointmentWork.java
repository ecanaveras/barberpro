package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class AppointmentWork {

    @EmbeddedId
    private AppointmentWorkId id = new AppointmentWorkId();

    @ManyToOne
    @MapsId("appointmentId")
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @MapsId("workId")
    @JoinColumn(name = "work_id")
    private Work work;

    private int quantity;

    public AppointmentWork() {}

    public AppointmentWork(Appointment appointment, Work work, int quantity) {
        this.appointment = appointment;
        this.work = work;
        this.quantity = quantity;
        this.id = new AppointmentWorkId(appointment.getId(), work.getId());
    }

    public AppointmentWorkId getAppoinmentId() {
        return id;
    }

    public void setId(AppointmentWorkId id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentWork that = (AppointmentWork) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(appointment, that.appointment) && Objects.equals(work, that.work);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appointment, work, quantity);
    }
}
