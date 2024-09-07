package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.type.TrueFalseConverter;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer extends AbstractEntity {

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
}
