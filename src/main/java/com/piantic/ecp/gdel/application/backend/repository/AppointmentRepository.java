package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Tenant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("select a from Appointment a JOIN a.profile p where p.id=:profileId")
    List<Appointment> findByProfileId(@Param("profileId") Long profileId);

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("select a from Appointment a JOIN a.profile p where p.id=:profileId and a.appointmentTime>=:start and a.appointmentTime<=:end order by a.appointmentTime desc")
    List<Appointment> findByProfileAndDate(@Param("profileId") Long profileId, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a WHERE a.id = :id")
    Appointment findByIdWithAssociations(@Param("id") Long id);

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a")
    List<Appointment> findAllWithAssociations();

    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a where a.id=:id")
    Appointment findbyId(Long id);


    @EntityGraph(attributePaths = {"customer", "profile", "appointmentWorks"})
    @Query("SELECT a FROM Appointment a where a.tenant=:tenant and a.appointmentTime>=:start and a.appointmentTime<=:end order by a.appointmentTime desc")
    List<Appointment> findByTenantAndDateRange(Tenant tenant, LocalDateTime start, LocalDateTime end);


    @Query(value = "select T2.NAME_PROFILE, FORMATDATETIME(T1.APPOINTMENT_TIME,'dd/MM/yyyy') APPOINTMENT_TIME, sum(TOTAL) TOTAL " +
            "from APPOINTMENT T1 LEFT JOIN PROFILE T2 ON T1.PROFILE_ID = T2.ID " +
            "WHERE T1.TENANT_ID=:tenantId and T1.APPOINTMENT_TIME>=:start and T1.APPOINTMENT_TIME<=:end "+
            "GROUP BY T2.NAME_PROFILE, FORMATDATETIME(T1.APPOINTMENT_TIME,'dd/MM/yyyy') ", nativeQuery = true)
    List<Object[]> findAggWorkProfileDate(Long tenantId, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT T4.NAME_PROFILE, UPPER(T3.TITLE) PRODUCT, SUM(T1.SUBTOTAL) SUBTOTAL, SUM(T1.REVENUE) REVENUE, SUM(T1.VALUE_COMMISION) VALUE_COMMISION " +
            "FROM APPOINTMENT_WORK T1 " +
            " INNER JOIN APPOINTMENT T0 ON T1.APPOINTMENT_ID=T0.ID AND T1.TENANT_ID=T0.TENANT_ID" +
            " LEFT JOIN PROFILE_PRODUCTS T2 ON T1.TENANT_ID=T2.TENANT_ID AND T2.PROFILE_ID=T0.PROFILE_ID AND T1.PRODUCT_ID=T2.PRODUCT_ID" +
            " LEFT JOIN PRODUCT T3 ON T2.PRODUCT_ID = T3.ID AND T1.TENANT_ID=T3.TENANT_ID" +
            " LEFT JOIN PROFILE T4 ON T2.PROFILE_ID = T4.ID AND T1.TENANT_ID=T4.TENANT_ID" +
            " WHERE T1.TENANT_ID=:tenantId and T0.APPOINTMENT_TIME>=:start and T0.APPOINTMENT_TIME<=:end  "+
            " GROUP BY  T4.NAME_PROFILE, T3.TITLE", nativeQuery = true)
    List<Object[]> findAggProductProfileDate(Long tenantId, LocalDateTime start, LocalDateTime end);
}
