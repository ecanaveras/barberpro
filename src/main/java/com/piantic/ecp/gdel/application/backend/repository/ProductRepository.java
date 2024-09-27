package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Product;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select t from Product t " +
            " where t.enabled=true and t.tenant=:tenant and lower(t.title) like lower(concat('%', :searchTerm, '%')) ")
    List<Product> search(Tenant tenant, @Param("searchTerm") String searchTerm);

    @Query("select a from Product a LEFT JOIN FETCH a.profilesProducts p where a.enabled=true and a.tenant=:tenantId")
    List<Product> findAllByTenant(@Param("tenantId") Tenant tenantId);

    @Query("select a from Product a LEFT JOIN FETCH a.profilesProducts p where a.enabled=true and a.tenant=:tenantId and lower(a.title) like lower(concat('%', :searchTerm, '%'))")
    List<Product> searchByTenant(@Param("tenantId") Tenant tenantId, @Param("searchTerm") String searchTerm);


    @Query("SELECT w FROM Product w LEFT JOIN FETCH w.profilesProducts p WHERE w.enabled=true and p.tenant=:tenantId and p.profile=:profileId")
    List<Product> findProductsByProfile(@Param("tenantId") Tenant tenantId, @Param("profileId") Profile profileId);

}
