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
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    private int quantity;

    @NotNull
    private Double subtotal;

    private Double revenue;

    private Double commissions;

    private Double valueCommision;

    private String comment;

    public AppointmentWork() {
    }

    public AppointmentWork(Appointment appointment, Product product, int quantity, Double subtotal) {
        this.appointment = appointment;
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public AppointmentWork(Appointment appointment, Product product, int quantity, Double subtotal, Double revenue, Double commissions, Double valueCommision) {
        this.appointment = appointment;
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.commissions = commissions;
        this.valueCommision = valueCommision;
        this.revenue = revenue;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Double getCommissions() {
        return commissions;
    }

    public void setCommissions(Double commissions) {
        this.commissions = commissions;
    }

    public Double getValueCommision() {
        return valueCommision;
    }

    public void setValueCommision(Double valueCommision) {
        this.valueCommision = valueCommision;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppointmentWork that = (AppointmentWork) o;
        return quantity == that.quantity && Objects.equals(appointment, that.appointment) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), appointment, product, quantity);
    }


}
