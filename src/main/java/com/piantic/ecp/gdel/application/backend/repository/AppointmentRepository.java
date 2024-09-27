package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("select a from Appointment a JOIN a.profile p where a.enabled=true and p.id=:profileId and p.tenant=:tenant")
    List<Appointment> findByProfileId(Tenant tenant, @Param("profileId") Long profileId);

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("select a from Appointment a JOIN a.profile p where a.enabled=true and a.tenant=:tenant and p.id=:profileId and a.appointmentTime>=:start and a.appointmentTime<=:end order by a.appointmentTime desc")
    List<Appointment> findByProfileAndDate(Tenant tenant, @Param("profileId") Long profileId, LocalDateTime start, LocalDateTime end);


    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a where a.enabled=true and a.id=:id and a.tenant=:tenant")
    Appointment findbyId(Tenant tenant, Long id);


    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a where a.enabled=true and a.tenant=:tenant and a.appointmentTime>=:start and a.appointmentTime<=:end order by a.appointmentTime desc")
    List<Appointment> findByTenantAndDateRange(Tenant tenant, LocalDateTime start, LocalDateTime end);


    @Query(value = "select T2.NAME_PROFILE, FORMATDATETIME(T1.APPOINTMENT_TIME,'dd/MM/yyyy') APPOINTMENT_TIME, sum(TOTAL) TOTAL " +
            "from APPOINTMENT T1 LEFT JOIN PROFILE T2 ON T1.PROFILE_ID = T2.ID AND T1.TENANT_ID=T2.TENANT_ID " +
            "WHERE T1.ENABLED=true AND T1.TENANT_ID=:tenantId and T1.APPOINTMENT_TIME>=:start and T1.APPOINTMENT_TIME<=:end "+
            "GROUP BY T2.NAME_PROFILE, FORMATDATETIME(T1.APPOINTMENT_TIME,'dd/MM/yyyy') ORDER BY 2,1", nativeQuery = true)
    List<Object[]> findAggWorkProfileDate(Long tenantId, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT T4.NAME_PROFILE, UPPER(T3.TITLE) PRODUCT, SUM(T1.SUBTOTAL) SUBTOTAL, SUM(T1.REVENUE) REVENUE, SUM(T1.VALUE_COMMISION) VALUE_COMMISION, COUNT(T1.ID) CANT_WORKS " +
            "FROM APPOINTMENT_WORK T1 " +
            " INNER JOIN APPOINTMENT T0 ON T1.APPOINTMENT_ID=T0.ID AND T1.TENANT_ID=T0.TENANT_ID" +
            " LEFT JOIN PRODUCT T3 ON T1.PRODUCT_ID = T3.ID AND T1.TENANT_ID=T3.TENANT_ID" +
            " LEFT JOIN PROFILE T4 ON T0.PROFILE_ID = T4.ID AND T1.TENANT_ID=T4.TENANT_ID" +
            " WHERE T0.ENABLED=true AND T1.TENANT_ID=:tenantId and T0.APPOINTMENT_TIME>=:start and T0.APPOINTMENT_TIME<=:end  "+
            " GROUP BY  T4.NAME_PROFILE, T3.TITLE", nativeQuery = true)
    List<Object[]> findAggProductProfileDate(Long tenantId, LocalDateTime start, LocalDateTime end);

    void deleteByTenantAndId(Tenant tenant, Long id);

    List<Appointment> findByTenantAndProfileAndAppointmentTimeBetweenAndEnabledTrueOrderByAppointmentTimeDesc(Tenant tenant, Profile profile, LocalDateTime appointmentTime, LocalDateTime appointmentTime2, Pageable pageable);
}
