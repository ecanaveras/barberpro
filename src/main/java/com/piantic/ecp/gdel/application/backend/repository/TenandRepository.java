package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenandRepository extends JpaRepository<Tenant, Integer> {

    Tenant findTenantByEmail(String email);

    Tenant findTenantByEmailAndEnabledTrue(@NotNull String email);
}
