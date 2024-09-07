package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.entity.Profile;
import com.piantic.ecp.gdel.application.backend.entity.Work;
import com.piantic.ecp.gdel.application.backend.repository.AppointmentRepository;
import com.piantic.ecp.gdel.application.backend.repository.CustomerRepository;
import com.piantic.ecp.gdel.application.backend.repository.ProfileRepository;
import com.piantic.ecp.gdel.application.backend.repository.WorkRepository;
import com.piantic.ecp.gdel.application.backend.utils.generics.GenericService;
import com.piantic.ecp.gdel.application.ui.views.WorkingView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService implements GenericService<Appointment> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkRepository workRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Guarda un appointment con una lista de trabajos.
     *
     * @param customerId      ID del cliente
     * @param stylistId       ID del estilista
     * @param worksAdded      Conjunto de trabajos añadidos
     * @param appointmentTime Momento de la cita
     * @return La cita creada
     */
    public Appointment createAppointment(Long customerId, Long stylistId, List<WorkingView.WorkAdded> worksAdded, LocalDateTime appointmentTime, Double total) {

        // Obtener cliente y estilista
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Profile stylist = profileRepository.findById(stylistId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado"));

        // Crear un nuevo Appointment
        Appointment appointment = new Appointment(appointmentTime, stylist, customer, total);

        // Procesar cada WorkAdded
        for (WorkingView.WorkAdded workAdded : worksAdded) {
            Work work = workAdded.getServicio();

            // Verificar si el Work está gestionado por la sesión de JPA, si no, usar merge
            if (!entityManager.contains(work)) {
                work = entityManager.merge(work);
            }

            // Añadir el trabajo al Appointment
            appointment.addWork(work, workAdded.getCant(), work.getPrice() * workAdded.getCant());
        }

        // Retornar la cita
        return appointment;
    }

    public void save(Appointment chamba) {
        appointmentRepository.save(chamba);
    }

    public List<Appointment> findAppointmentByProfile(Profile profile) {
        return appointmentRepository.findByProfileId(profile.getId());
    }

    public List<Appointment> findAppointmentByProfileDate(Profile profile, LocalDate localDate) {
        //TODO Buscar por perfil y fecha.
        return appointmentRepository.findByProfileId(profile.getId());
    }


    @Override
    public List<Appointment> findAll(String filter) {
        return appointmentRepository.findAll();
    }

    @Override
    public Long getId(Appointment entity) {
        return entity.getId();
    }
}

