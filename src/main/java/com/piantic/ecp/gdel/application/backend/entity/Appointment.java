package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Appointment extends BaseEntity {

    private LocalDateTime appointmentTime;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppointmentWork> appointmentWorks = new HashSet<>();

    @NotNull
    private Double total;

    public Appointment() {}

    public Appointment(LocalDateTime datework, Profile profile, Customer customer, Double total) {
        this.appointmentTime = datework;
        this.profile = profile;
        this.customer = customer;
        this.total = total;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<AppointmentWork> getAppointmentWorks() {
        return appointmentWorks;
    }

    public void setAppointmentWorks(Set<AppointmentWork> appointmentWorks) {
        this.appointmentWorks = appointmentWorks;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Permite agregar trabajos realizados por el perfil.
     * @param product
     * @param quantity
     */
    public void addWork(Product product, int quantity, Double total) {
        AppointmentWork appointmentWork = new AppointmentWork(this, product, quantity, total);
        appointmentWorks.add(appointmentWork);
    }


    /**
     * Permite agregar trabajos realizados por el perfil.
     * @param product
     * @param quantity
     * @param total
     * @param revenue
     * @param commssion
     * @param valCommision
     */
    public void addWork(Product product, int quantity, Double total, Double revenue, Double commssion, Double valCommision) {
        AppointmentWork appointmentWork = new AppointmentWork(this, product, quantity, total, revenue, commssion, valCommision);
        appointmentWorks.add(appointmentWork);
    }
}
