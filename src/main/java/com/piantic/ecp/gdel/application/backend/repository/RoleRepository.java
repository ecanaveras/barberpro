package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    @Query("select r from Role r LEFT JOIN FETCH r.profiles " +
            " where lower(r.name) like lower(concat('%', :searchTerm, '%'))")
    List<Role> search(@Param("searchTerm") String searchTerm);

    // Método para obtener un Role junto con sus servicios (fetch join)
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.profiles WHERE r.id = :id")
    Optional<Role> findByIdWithProfile(@Param("id") Long id);

}
