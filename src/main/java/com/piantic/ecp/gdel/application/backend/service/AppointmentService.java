package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.repository.AppointmentRepository;
import com.piantic.ecp.gdel.application.backend.repository.CustomerRepository;
import com.piantic.ecp.gdel.application.backend.repository.ProfileRepository;
import com.piantic.ecp.gdel.application.backend.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkRepository workRepository;

    public Appointment createAppointment(Long customerId, Long stylistId, Map<Long, Integer> worksWithQuantity, LocalDateTime appointmentTime) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Profile stylist = profileRepository.findById(stylistId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado"));

        Appointment appointment = new Appointment(appointmentTime, stylist, customer);

        for (Map.Entry<Long, Integer> entry : worksWithQuantity.entrySet()) {
            Work work = workRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado"));

            // Verificar si el estilista tiene permitido realizar este trabajo
            appointment.addWork(work, entry.getValue());
        }

        return appointmentRepository.save(appointment);
    }

}
