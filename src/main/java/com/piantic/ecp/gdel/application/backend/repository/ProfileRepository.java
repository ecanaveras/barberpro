package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("select p from Profile p " +
            " where p.enabled=true and p.tenant=:tenant and lower(p.nameProfile) like lower(concat('%', :searchTerm, '%')) " +
            " or lower(p.status) like lower(concat('%',:searchTerm,'%'))")
    List<Profile> search(Tenant tenant, @Param("searchTerm") String searchTerm);


    // Método para obtener un Perfil junto con sus roles (fetch join)
    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.roles WHERE p.enabled=true and p.tenant=:tenant and p.id = :id")
    Optional<Profile> findByIdWithProfile(Tenant tenant, @Param("id") Long id);


    // Método para obtener un Role junto con sus servicios (fetch join)
    @Query(value = "SELECT p.* FROM Role_profile rp " +
            "left join Profile p on rp.profile_id=p.id and rp.tenant_id=p.tenant_id " +
            "left join Role r on rp.role_id=r.id and rp.tenant_id=r.tenant_id " +
            "where p.enabled=true and r.enabled=true and rp.role_id=:id and rp.tenant_id=:tenant"
            , nativeQuery = true)
    List<Profile> findByRoleId(Tenant tenant, @Param("id") Long id);

    List<Profile> findByTenantAndEnabledTrue(Tenant tenant);

    List<Profile> findByTenantAndStatusAndEnabledTrue(Tenant tenant, @NotNull Profile.Status status);

    @Query("SELECT p from Profile p LEFT JOIN FETCH p.products a where a.enabled=true and a.tenant=:tenant and a.product = :productId")
    List<Profile> findProfilesByProduct(Tenant tenant, Product productId);
}
