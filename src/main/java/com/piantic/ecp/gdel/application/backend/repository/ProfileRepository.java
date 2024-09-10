package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("select p from Profile p " +
            " where lower(p.nameProfile) like lower(concat('%', :searchTerm, '%')) " +
            " or lower(p.status) like lower(concat('%',:searchTerm,'%'))")
    List<Profile> search(@Param("searchTerm") String searchTerm);


    // Método para obtener un Perfil junto con sus roles (fetch join)
    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.roles WHERE p.id = :id")
    Optional<Profile> findByIdWithProfile(@Param("id") Long id);


    // Método para obtener un Role junto con sus servicios (fetch join)
    @Query(value = "SELECT p.* FROM Role_profile rp " +
            "left join Profile p on rp.profile_id=p.id " +
            "left join Role r on rp.role_id=r.id " +
            "where rp.role_id=:id"
            , nativeQuery = true)
    List<Profile> findByRoleId(@Param("id") Long id);

    List<Profile> findByTenant(Tenant tenant);
}
