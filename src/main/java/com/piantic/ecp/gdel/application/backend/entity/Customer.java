package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.type.TrueFalseConverter;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer extends BaseEntity {

    @NotNull
    @NotEmpty
    private String name;

    private String phone;

    @Email
    private String email;

    @Convert(converter = TrueFalseConverter.class)
    private Boolean favorite;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @Column(name = "enabled", nullable = false, columnDefinition = "INT(1)")
    private Boolean enabled = true;

    public Customer() {}

    public Customer(String name, String phone, String email, Boolean favorite) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public Boolean isFavorite() {
        return favorite != null && favorite;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
