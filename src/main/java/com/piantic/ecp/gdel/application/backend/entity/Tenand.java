package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tenand extends AbstractEntity {

    @NotNull
    private String nameTenand;

    @NotNull
    private String email;

    // Campo para almacenar la imagen como bytes
    @Lob
    @Column(name = "logo", columnDefinition = "BLOB")
    private byte[] logo;

    public Tenand() {}

    public Tenand(String nameTenand, String email) {
        this.nameTenand = nameTenand;
        this.email = email;
    }

    public @NotNull String getNameTenand() {
        return nameTenand;
    }

    public void setNameTenand(@NotNull String nameTenand) {
        this.nameTenand = nameTenand;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}

