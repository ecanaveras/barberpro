package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenandRepository extends JpaRepository<Tenant, Integer> {

    Tenant findTenantByEmail(String email);
}
