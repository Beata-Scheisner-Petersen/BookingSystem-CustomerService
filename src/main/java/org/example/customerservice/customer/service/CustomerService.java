package org.example.customerservice.customer.service;

import org.example.customerservice.customer.model.Customer;
import org.example.customerservice.customer.model.dto.CreateCustomerRequest;
import org.example.customerservice.customer.model.dto.CreateCustomerResponse;
import org.example.customerservice.customer.model.dto.CustomerUpdateRequest;
import org.example.customerservice.customer.model.dto.ReservationStatusRequest;
import org.example.customerservice.customer.repository.CustomerRepository;
import org.example.customerservice.exceptionhandler.customexeptions.AlreadyExistException;
import org.example.customerservice.exceptionhandler.customexeptions.BadRequestException;
import org.example.customerservice.exceptionhandler.customexeptions.HaveReservationException;
import org.example.customerservice.exceptionhandler.customexeptions.NotFoundException;
import org.example.customerservice.security.password.PasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordService passwordService;
    private final RestTemplate template = new RestTemplate();

    public CustomerService(CustomerRepository customerRepository, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.passwordService = passwordService;
    }

    public CreateCustomerResponse createNewCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByEmail(request.email())) {
            throw new AlreadyExistException("Email already exist");

        } else if (customerRepository.existsByIdentificationNumber(request.identificationNumber())) {
            throw new AlreadyExistException("Identification number already exist in the system");

        } else if (request.phoneNumber() != null && customerRepository.existsByPhoneNumber(request.phoneNumber())) {
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
        customerRepository.save(customer);

        return new CreateCustomerResponse("account successfully created", true);
    }

    @Transactional
    public void updateCustomerInfo(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));

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
            customer.setPassword(passwordService.hash(request.password()));
        }

        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("id not found"));

        ReservationStatusRequest status = null;
        try {
            status = template.getForObject("http://localhost:8080/customer/" + id + "/active", ReservationStatusRequest.class);
        } catch (RestClientException e) {
            System.out.println("RestClientException");
            throw new BadRequestException("Could not connect to Reservation service");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (status == null) {
            throw new BadRequestException("Could not get status from reservation service");
        }

        if (status.hasActiveReservations()) {
            throw new HaveReservationException("You can't delete your account while having active bookings");
        }

        customerRepository.delete(customer);
    }

    public Customer getCustomerInformation(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    public boolean doesCustomerExist(Long id) {
        if (id == null) {
            throw new RuntimeException("could not get valid id");
        }
        return customerRepository.existsById(id);
    }
}
