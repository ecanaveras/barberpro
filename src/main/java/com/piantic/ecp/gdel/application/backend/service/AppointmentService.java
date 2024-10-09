package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.*;
import com.piantic.ecp.gdel.application.backend.repository.AppointmentRepository;
import com.piantic.ecp.gdel.application.backend.repository.CustomerRepository;
import com.piantic.ecp.gdel.application.backend.repository.ProductRepository;
import com.piantic.ecp.gdel.application.backend.repository.ProfileRepository;
import com.piantic.ecp.gdel.application.backend.utils.generics.GenericService;
import com.piantic.ecp.gdel.application.ui.views.WorkingView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private ProductRepository productRepository;

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
            Product product = workAdded.getServicio();

            // Verificar si el Work está gestionado por la sesión de JPA, si no, usar merge
            if (!entityManager.contains(product)) {
                product = entityManager.merge(product);
            }

            // Añadir el trabajo al Appointment
            appointment.addWork(product, workAdded.getCant(), product.getPrice() * workAdded.getCant());
        }

        // Retornar la cita
        return appointment;
    }

    public void save(Appointment chamba) {
        appointmentRepository.save(chamba);
    }

    public List<Appointment> findAppointmentByProfile(Profile profile) {
        return appointmentRepository.findByProfileId(Application.getTenant(), profile.getId());
    }

    public List<Appointment> findAppointmentByProfileAndDate(Long profileId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return appointmentRepository.findByProfileAndDate(Application.getTenant(), profileId, startOfDay, endOfDay);
    }

    /**
     * Busca las ultimas 10 citas de un estilista
     * @param profile
     * @param start
     * @param end
     * @return
     */
    public List<Appointment> findLastTenAppointmentsOfCurrentMonth(Profile profile, LocalDateTime start, LocalDateTime end){
        PageRequest topTen = PageRequest.of(0, 10);
        return  appointmentRepository.findByTenantAndProfileAndAppointmentTimeBetweenAndEnabledTrueOrderByAppointmentTimeDesc(Application.getTenant(), profile, start, end, topTen);
    }

    /**
     * BUscar las ultimas 10 citas de un cliente
     * @param customer
     * @param start
     * @param end
     * @return
     */
    public List<Appointment> findLastTenAppointmentsOfCurrentMonth(Customer customer, LocalDateTime start, LocalDateTime end){
        PageRequest topTen = PageRequest.of(0, 10);
        return  appointmentRepository.findByTenantAndCustomerAndAppointmentTimeBetweenAndEnabledTrueOrderByAppointmentTimeDesc(Application.getTenant(), customer, start, end, topTen);
    }


    /**
     * Cuenta las citas de un cliente
     * @param tenant
     * @param customer
     * @return
     */
    public Integer countByCustomer(Tenant tenant, Customer customer) {
        return appointmentRepository.countByTenantAndCustomerAndEnabledTrue(tenant, customer);
    }

    /**
     * Buscar una cita por su ID
     * @param appointmentId
     * @return
     */
    public Appointment findByAppointmentId(Long appointmentId) {
        return appointmentRepository.findbyId(Application.getTenant(), appointmentId);
    }

    @Override
    public List<Appointment> findAll(String filter) {
        return appointmentRepository.findAll();
    }

    @Override
    public Long getId(Appointment entity) {
        return entity.getId();
    }

    public List<Appointment> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByTenantAndDateRange(Application.getTenant(), start, end);
    }

    public List<Object[]> findAggWorkProfileDate(LocalDate start, LocalDate end) {
        LocalDateTime inicio = start.atStartOfDay();
        LocalDateTime fin = end.atTime(LocalTime.MAX);
        return appointmentRepository.findAggWorkProfileDate(Application.getTenant().getId(), inicio, fin);
    }

    public List<Object[]> findAggProductProfileDate(LocalDate start, LocalDate end){
        LocalDateTime inicio = start.atStartOfDay();
        LocalDateTime fin = end.atTime(LocalTime.MAX);
        return appointmentRepository.findAggProductProfileDate(Application.getTenant().getId(), inicio, fin);
    }

    public void delete(Appointment appointment) {
        appointmentRepository.deleteByTenantAndId(Application.getTenant(), appointment.getId());
    }
}

