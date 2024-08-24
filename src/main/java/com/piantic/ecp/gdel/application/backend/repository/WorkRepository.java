package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query("select t from Work t " +
            " where lower(t.title) like lower(concat('%', :searchTerm, '%')) ")
    List<Work> search(@Param("searchTerm") String searchTerm);


    // Método para obtener todos los servicios que no tiene un ROLE (fetch join)
    @Query("SELECT w FROM Work w WHERE w.id NOT IN (SELECT w2.id FROM Role r JOIN r.works w2 WHERE r.id = :roleId)")
    List<Work> findWorksNotAssignedToRoleId(@Param("roleId") Long roleId);

}
