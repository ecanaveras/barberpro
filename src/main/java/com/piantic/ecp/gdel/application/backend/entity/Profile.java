package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Profile extends BaseEntity {

    public enum Status {
        Activo, Inactivo
    }

    @NotNull
    @NotEmpty
    private String nameProfile;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Profile.Status status;

    private String pin;

    @Email
    private String email;

    private String phone;

    @ManyToMany(mappedBy = "profiles")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProfileProduct> products = new HashSet<>();

    public Profile() {}

    public Profile(String name, Profile.Status status, String pin) {
        this.nameProfile = name;
        this.status = status;
        this.pin = pin;
    }

    public Set<ProfileProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<ProfileProduct> products) {
        this.products = products;
    }

    public String getNameProfile() {
        return nameProfile;
    }

    public void setNameProfile(@NotNull @NotEmpty String nameProfile) {
        this.nameProfile = nameProfile;
    }

    public String getPin() {
        return pin;
    }

    public Boolean isLock() {
        return pin != null && !pin.isEmpty();
    }

    public Profile.Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull Profile.Status status) {
        this.status = status;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Profile profile = (Profile) o;
        return Objects.equals(nameProfile, profile.nameProfile) && status == profile.status && Objects.equals(pin, profile.pin) && Objects.equals(email, profile.email) && Objects.equals(phone, profile.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nameProfile, status, pin, email, phone);
    }
}
