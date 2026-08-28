package org.example.customerservice.customer.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.example.customerservice.customer.model.Customer;
import org.example.customerservice.customer.model.dto.CreateCustomerRequest;
import org.example.customerservice.customer.model.dto.CustomerLoginRequest;
import org.example.customerservice.customer.model.dto.CustomerUpdateRequest;
import org.example.customerservice.customer.service.CustomerService;

import org.example.customerservice.exceptionhandler.customexeptions.AlreadyExistException;
import org.example.customerservice.exceptionhandler.customexeptions.HaveReservationException;
import org.example.customerservice.exceptionhandler.customexeptions.WrongEmailOrPasswordException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(
            @Valid @RequestBody CreateCustomerRequest customer,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            result.getFieldErrors()
                    .forEach(
                            error -> errors.put(
                                    error.getField(),
                                    error.getDefaultMessage()
                            )
                    );

            return (ResponseEntity
                    .badRequest()
                    .body(errors)
            );
        }
        return (ResponseEntity
                .status(
                        HttpStatus.CREATED
                ).body(
                        customerService.createNewCustomer(customer)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody CustomerLoginRequest request,
            BindingResult result,
            HttpSession session
    ) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            result.getFieldErrors()
                    .forEach(
                            error -> errors.put(
                                    error.getField(),
                                    error.getDefaultMessage()
                            )
                    )
            ;

            return (ResponseEntity
                    .badRequest()
                    .body(errors)
            );
        }

        try {
            Customer customer = customerService.loginCustomer(
                    request.email(),
                    request.password()
            );

            session.setAttribute(
                    "customerId",
                    customer.getId()
            );

            return (ResponseEntity.ok()
                    .body(
                            Map.of(
                                    "message",
                                    "login successful"
                            )
                    )
            );

        } catch (WrongEmailOrPasswordException e) {
            return (ResponseEntity
                    .status(409)
                    .body(
                            e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCustomer(
            HttpSession session,
            CustomerUpdateRequest request
    ) {
        Long id = (Long) session.getAttribute("customerId");

        if (id == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        }

        try {
            customerService.updateCustomerInfo(id, request);
            return ResponseEntity.ok(Map.of("success", true));

        } catch (AlreadyExistException error) {

            if (error.getMessage().contains("Email")) {
                return ResponseEntity.badRequest().body(Map.of("emailError", error.getMessage()));
            }

            if (error.getMessage().contains("Phone")) {
                return ResponseEntity.badRequest().body(Map.of("phoneError", error.getMessage()));
            }

            return ResponseEntity.badRequest().body(Map.of("error", "Unknown error"));
        }
    }


    @DeleteMapping
    public ResponseEntity<?> deleteCustomer(HttpSession session) {

        Long id = (Long) session.getAttribute(
                "customerId"
        );

        if (id == null) {
            return (ResponseEntity
                    .status(
                            HttpStatus.NETWORK_AUTHENTICATION_REQUIRED
                    ).body(
                            Map.of(
                                    "error",
                                    "authorization failed"
                            )
                    )
            );
        }

        try {
            customerService.deleteCustomer(id);

            return (ResponseEntity.ok().body(Map.of("message", "account deleted")));

        } catch (HaveReservationException e) {
            return (ResponseEntity.status(409).body(Map.of("error", e.getMessage())));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(500).body(Map.of("error", e.getMessage())));
        }
    }

    @PostMapping("/getId")
    public ResponseEntity<?> getId(HttpSession session) {
        try {
            Long id = customerService.getId(session);
            return ResponseEntity
                    .status(
                            200
                    ).body(
                            Map.of(
                                    "success",
                                    id
                            )
                    );
        } catch (IllegalStateException e) {
            return (ResponseEntity
                    .status(
                            401
                    ).body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    )
            );
        }
    }
}
