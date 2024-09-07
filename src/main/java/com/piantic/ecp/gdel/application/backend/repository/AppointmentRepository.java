package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("select a from Appointment a JOIN a.profile p where p.id=:profileId")
    List<Appointment> findByProfileId(@Param("profileId") Long profileId);

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a WHERE a.id = :id")
    Appointment findByIdWithAssociations(@Param("id") Long id);

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a")
    List<Appointment> findAllWithAssociations();


}
