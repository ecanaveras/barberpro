package com.piantic.ecp.gdel.application.backend.repository.setting;

import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import com.piantic.ecp.gdel.application.backend.entity.setting.ConfigOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigOptionRepository extends JpaRepository<ConfigOption, Long> {

    Optional<ConfigOption> findByTenantAndNameAndEnabledTrue(Tenant tenant, String name);
}
