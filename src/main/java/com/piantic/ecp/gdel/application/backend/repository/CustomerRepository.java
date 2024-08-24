package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c " +
            " where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            " or lower(c.phone) like lower(concat('%',:searchTerm,'%')) ")
    List<Customer> search(@Param("searchTerm") String searchTerm);

    Customer findByName(String name);

    Optional<Customer> findById(Long id);

    List<Customer> findByNameStartingWith(String prefix);

    List<Customer> findByNameEndingWith(String suffix);

    List<Customer> findByNameContaining(String infix);

    List<Customer> findByFavoriteTrue();

    List<Customer> findByFavoriteFalse();
}
