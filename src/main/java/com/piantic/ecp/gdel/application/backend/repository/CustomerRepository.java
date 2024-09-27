package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c " +
            " where c.enabled=true and c.tenant=:tenant and lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            " or lower(c.phone) like lower(concat('%',:searchTerm,'%')) ")
    List<Customer> search(Tenant tenant, @Param("searchTerm") String searchTerm);

    List<Customer> findByTenantAndEnabledTrue(Tenant tenant);

    Customer findByTenantAndName(Tenant tenant, @NotNull @NotEmpty String name);

    Customer findByTenantAndId(Tenant tenant, Long id);

    List<Customer> findByNameStartingWith(String prefix);

    List<Customer> findByNameEndingWith(String suffix);

    List<Customer> findByNameContaining(String infix);

    List<Customer> findByFavoriteTrue();

    List<Customer> findByFavoriteFalse();
}
