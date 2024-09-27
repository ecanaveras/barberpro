package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "profile_products")
public class ProfileProduct{


    @EmbeddedId
    private ProfileProductId id = new ProfileProductId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("profileId")
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne()
    @MapsId("tenantId")
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "enabled", nullable = false, columnDefinition = "INT(1)")
    private Boolean enabled = true;

    public ProfileProduct() {
    }

    public ProfileProduct(Profile profile, Product product, Tenant tenant) {
        this.profile = profile;
        this.product = product;
        this.tenant = tenant;
        this.id = new ProfileProductId(profile.getId(), product.getId(), tenant.getId());
    }

    public void setId(ProfileProductId id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileProduct that = (ProfileProduct) o;
        return Objects.equals(profile, that.profile) && Objects.equals(product, that.product) && Objects.equals(tenant, that.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, product, tenant);
    }
}
