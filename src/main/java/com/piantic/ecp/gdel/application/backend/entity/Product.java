package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product extends BaseEntity {

    @NotNull
    @NotEmpty
    private String title;
    private String description;
    private Double price;
    private String image;
    private String observations;
    private Double commissions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<AppointmentWork> appointmentWorks = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProfileProduct> profilesProducts = new HashSet<>();

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

    public Set<Profile> getProfiles() {
        Set<Profile> profiles = new HashSet<>();
        profilesProducts.forEach(profileProduct -> profiles.add(profileProduct.getProfile()));
        return profiles;
    }

    public Set<ProfileProduct> getProfilesProducts() {
        return profilesProducts;
    }

    public void setProfilesProducts(Set<ProfileProduct> profilesProducts) {
        this.profilesProducts = profilesProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(title, product.title) && Objects.equals(description, product.description) && Objects.equals(price, product.price) && Objects.equals(image, product.image) && Objects.equals(observations, product.observations) && Objects.equals(commissions, product.commissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, price, image, observations, commissions);
    }
}
