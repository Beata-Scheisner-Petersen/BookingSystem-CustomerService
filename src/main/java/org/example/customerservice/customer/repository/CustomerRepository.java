package org.example.customerservice.customer.repository;

import org.example.customerservice.customer.model.Customer;
import org.example.customerservice.customer.model.dto.CustomerInfoRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByIdentificationNumber(String identificationNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Customer> findByEmail(String email);

    Optional<CustomerInfoRequest> getCustomersById(Long id);
}
