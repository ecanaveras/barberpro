package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
