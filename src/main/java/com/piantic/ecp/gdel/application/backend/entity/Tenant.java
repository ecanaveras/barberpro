package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty(message = "Por favor indique el Nombre de su Negocio")
    private String nameTenant;

    @NotNull
    private String email;

    private String phone;

    private String address;

    private String city;

    private String nit;

    // Campo para almacenar la imagen como bytes
    @Lob
    @Column(name = "logo", columnDefinition = "BLOB")
    private byte[] logo;

    public Tenant() {}

    public Tenant(String nameTenant, String email) {
        this.nameTenant = nameTenant;
        this.email = email;
    }

    public @NotNull String getNameTenant() {
        return nameTenant;
    }

    public void setNameTenant(@NotNull String nameTenant) {
        this.nameTenant = nameTenant;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}

