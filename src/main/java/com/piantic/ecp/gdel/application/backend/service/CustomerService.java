package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.Application;
import com.piantic.ecp.gdel.application.backend.entity.Appointment;
import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.repository.CustomerRepository;
import com.piantic.ecp.gdel.application.backend.utils.generics.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomerService implements GenericService<Customer> {

    public static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());

    private CustomerRepository customerRepository;

    @Autowired
    private AppointmentService appointmentService;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll(String textfilter) {
        if (textfilter == null || textfilter.isEmpty()) {
            return customerRepository.findByTenantAndEnabledTrue(Application.getTenant());
        } else {
            return customerRepository.search(Application.getTenant(), textfilter);
        }
    }

    public long count() {
        return customerRepository.count();
    }

    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer save(Customer customer) {
        if (customer == null) {
            LOGGER.log(Level.SEVERE, "Cliente es nulo, asegurese que los datos sean correctos");
            return null;
        }
        return customerRepository.save(customer);
    }

    public Customer findByCustomerId(Long id) {
        return customerRepository.findByTenantAndId(Application.getTenant(), id);
    }

    public Long getId(Customer customer) {
        return customer.getId();
    }

    @Transactional
    public void disableCustomerCascade(Long customerId) {
        Customer customer = customerRepository.findByTenantAndId(Application.getTenant(), customerId);
        if (customer == null) {
            new IllegalArgumentException("Customer not found");
        }

        // Deshabilitar el Customer
        customer.setEnabled(false);
        customerRepository.save(customer);

        // Deshabilitar en cascada las Citas asociadas
        Set<Appointment> citas = customer.getAppointments();
        for (Appointment cita : citas) {
            cita.setEnabled(false);
            appointmentService.save(cita);
        }
    }

/*
    @PostConstruct
    public void populateTestData() {
        if (customerRepository.count() == 0) {
//            Random r = new Random(0);
//            List<Customer> profiles = customerRepository.findAll();
            customerRepository.saveAll(Stream.of("Adolfo Paquecho", "Henry Cavil", "Fabio Díaz").map(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setFavorite(name.contains("o"));
                customer.setPhone("300 " + new Random().nextInt(1000));
                return customer;
            }).collect(Collectors.toList()));
        }
    }

 */

}
