package com.piantic.ecp.gdel.application.backend.service;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import com.piantic.ecp.gdel.application.backend.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomerService {
    public static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll(String textfilter) {
        if (textfilter == null || textfilter.isEmpty()) {
            return customerRepository.findAll();
        } else {
            return customerRepository.search(textfilter);
        }
    }

    public long count() {
        return customerRepository.count();
    }

    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    public void save(Customer customer) {
        if (customer == null) {
            LOGGER.log(Level.SEVERE, "Cliente es nulo, asegurese que los datos sean correctos");
            return;
        }
        customerRepository.save(customer);
    }

    public Customer findByCustomerId(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            return customer.get();
        }
        return null;
    }


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

}
