package com.piantic.ecp.gdel.application.backend.entity;

import jakarta.persistence.*;

import java.util.Objects;

@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public Long getId() {
        return id;
    }

    private boolean isPersisted() {
        return id != null;
    }

    @ManyToOne()
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(tenant, that.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenant);
    }
}
