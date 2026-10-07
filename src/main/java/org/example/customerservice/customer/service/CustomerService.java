package org.example.customerservice.customer.service;

import org.example.customerservice.customer.model.Customer;
import org.example.customerservice.customer.model.dto.CreateCustomerRequest;
import org.example.customerservice.customer.model.dto.CustomerInfoResponse;
import org.example.customerservice.customer.model.dto.CustomerUpdateRequest;
import org.example.customerservice.customer.repository.CustomerRepository;
import org.example.customerservice.exceptionhandler.customexeptions.AlreadyExistException;
import org.example.customerservice.exceptionhandler.customexeptions.NotFoundException;
import org.example.customerservice.security.password.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordService passwordService;
    final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(CustomerRepository customerRepository, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.passwordService = passwordService;
    }

    public void createNewCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByEmail(request.email())) {
            logger.warn("At create customer: Email already exist.");
            throw new AlreadyExistException("Email already exist");

        } else if (customerRepository.existsByIdentificationNumber(request.identificationNumber())) {
            logger.warn("At create customer: Identification number already exist in the system");
            throw new AlreadyExistException("Identification number already exist in the system");

        } else if (request.phoneNumber() != null && customerRepository.existsByPhoneNumber(request.phoneNumber())) {
            logger.warn("At create customer: Phone number already exist");
            throw new AlreadyExistException("Phone number already exist");
        }

        Customer customer = new Customer(
                request.firstname(),
                request.lastname(),
                request.identificationNumber(),
                request.email(),
                passwordService.hash(request.password()),
                request.phoneNumber()
        );

        Customer savedCustomer = customerRepository.save(customer);
        logger.info("customer created with id: {}", savedCustomer.getId());
    }

    @Transactional
    public void updateCustomerInfo(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            logger.error("At update customer: Customer with id {} not found", id);
                            return new RuntimeException("Customer not found");
                        }
                );

        if (request.firstname() != null && !request.firstname().isBlank()) {
            customer.setFirstname(request.firstname());
        }

        if (request.lastname() != null && !request.lastname().isBlank()) {
            customer.setLastname(request.lastname());
        }

        if (request.email() != null && !request.email().isBlank()) {

            if (customerRepository.existsByEmail(request.email())) {
                throw new AlreadyExistException("Email already exist");
            }
            customer.setEmail(request.email());
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {

            if (customerRepository.existsByPhoneNumber(request.phoneNumber())) {
                throw new AlreadyExistException("Phone number already exist");
            }
            customer.setPhoneNumber(request.phoneNumber());
        }

        if (request.password() != null && !request.password().isBlank()) {
            customer.setPassword(passwordService
                    .hash(request.password())
            );
        }
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("id not found")
                );
        logger.info("account with id {} is deleted", id);
        customerRepository.delete(customer);
    }

    public Customer getCustomerInformation(String email) {
        return (customerRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new NotFoundException("Customer not found")
                )
        );
    }

    public boolean doesCustomerExist(Long id) {
        if (id == null) {
            throw new RuntimeException("could not get valid id");
        }
        return customerRepository.existsById(id);
    }

    public CustomerInfoResponse getInfo(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
        return new CustomerInfoResponse(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }

    private String formatBearerToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token;
        }
        return "Bearer " + token;
    }
}
