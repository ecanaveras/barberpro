package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

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
}
