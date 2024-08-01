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
}
