package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("select p from Profile p " +
            " where lower(p.nameProfile) like lower(concat('%', :searchTerm, '%')) " +
            " or lower(p.status) like lower(concat('%',:searchTerm,'%'))")
    List<Profile> search(@Param("searchTerm") String searchTerm);
}
