package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Profile extends AbstractEntity implements Cloneable{

    public enum Status {
        Activo, Inactivo
    }

    @NotNull
    @NotEmpty
    private String nameProfile;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Profile.Status status;

    @NumberFormat
    private Integer pin;

    @Email
    private String email;

    private String phone;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_profile",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    public Profile() {}

    public Profile(String name, Profile.Status status, Integer pin) {
        this.nameProfile = name;
        this.status = status;
        this.pin = pin;
    }


    /**
     * Devuelve los trabajos permitidos para el perfil
     * @return
     */
    public Set<Work> getAllowedWorks(){
        Set<Work> allowedWorks = new HashSet<>();
        for(Role role : roles){
            allowedWorks.addAll(role.getWorks());
        }
        return allowedWorks;
    }

    public String getNameProfile() {
        return nameProfile;
    }

    public void setNameProfile(@NotNull @NotEmpty String nameProfile) {
        this.nameProfile = nameProfile;
    }

    public Integer getPin() {
        return pin;
    }

    public Boolean isLock() {
        return pin != null;
    }

    public Profile.Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull Profile.Status status) {
        this.status = status;
    }

    public void setPin(Integer pin) {
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
}
