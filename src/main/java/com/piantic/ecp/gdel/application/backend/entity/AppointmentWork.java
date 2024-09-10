package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
public class AppointmentWork extends BaseEntity {

//    @EmbeddedId
//    private AppointmentWorkId id = new AppointmentWorkId();

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Work work;

    private int quantity;

    @NotNull
    private Double subtotal;

    public AppointmentWork() {
    }

    public AppointmentWork(Appointment appointment, Work work, int quantity, Double subtotal) {
        this.appointment = appointment;
        this.work = work;
        this.quantity = quantity;
        this.subtotal = subtotal;
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppointmentWork that = (AppointmentWork) o;
        return quantity == that.quantity && Objects.equals(appointment, that.appointment) && Objects.equals(work, that.work);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), appointment, work, quantity);
    }

    /*@Override
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

     */
}
