package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Work extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String title;
    private String description;
    private Double price;
    private String image;
    private String observations;
    private Double commissions;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private Set<AppointmentWork> appointmentWorks = new HashSet<>();

   public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price != null ? price : 0.0;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Double getCommissions() {
        return commissions != null ? commissions : 0.0;
    }

    public void setCommissions(Double commissions) {
        this.commissions = commissions;
    }

    public Set<AppointmentWork> getAppointmentWorks() {
        return appointmentWorks;
    }

    public void setAppointmentWorks(Set<AppointmentWork> appointmentWorks) {
        this.appointmentWorks = appointmentWorks;
    }
}
