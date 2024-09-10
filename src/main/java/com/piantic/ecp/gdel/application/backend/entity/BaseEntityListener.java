package com.piantic.ecp.gdel.application.backend.entity;


import com.piantic.ecp.gdel.application.Application;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class BaseEntityListener {

    @PrePersist
    @PreUpdate
    public void setTenant(BaseEntity entity) {
        Tenant tenant = Application.getTenand();
        if (tenant != null && entity.getTenant() == null) {
            entity.setTenant(tenant);
        }
    }

}
