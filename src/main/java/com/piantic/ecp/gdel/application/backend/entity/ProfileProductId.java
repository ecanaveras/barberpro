package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProfileProductId implements Serializable {

    private Long profileId;
    private Long productId;
    private Long tenantId;

    public ProfileProductId() {
    }

    public ProfileProductId(Long profileId, Long productId, Long tenantId) {
        this.profileId = profileId;
        this.productId = productId;
        this.tenantId = tenantId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileProductId that = (ProfileProductId) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(productId, that.productId) && Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, productId, tenantId);
    }
}
