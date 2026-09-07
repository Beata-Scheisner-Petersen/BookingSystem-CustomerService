package org.example.customerservice.customer.controller;

import jakarta.validation.*;
import org.example.customerservice.customer.model.dto.*;
import org.example.customerservice.customer.service.*;
import org.example.customerservice.exceptionhandler.customexeptions.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;

    }

    @GetMapping("/test")
    public String test() {
        return "test customer-service";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerRequest customer, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return (ResponseEntity.badRequest().body(errors));
        }


        return (ResponseEntity.status(HttpStatus.CREATED).body(customerService.createNewCustomer(customer)));
    }

    @GetMapping("/does-customer-exist")
    public boolean doesCustomerExist(@AuthenticationPrincipal Long userId) {
        return customerService.doesCustomerExist(userId);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCustomer(@AuthenticationPrincipal Long id, @RequestBody CustomerUpdateRequest request) {
        if (id == null) {
            System.err.println("\n id null \n");
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomer(@AuthenticationPrincipal Long id, @RequestHeader("Authorization") String token) {
        if (id == null) {
            return (ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body(Map.of("error", "authorization failed")));
        }

        try {
            customerService.deleteCustomer(id, token);

            return (ResponseEntity.ok().body(Map.of("message", "account deleted")));

        } catch (HaveReservationException e) {
            return (ResponseEntity.status(409).body(Map.of("error", e.getMessage())));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(500).body(Map.of("error", e.getMessage())));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/info")
    public CustomerInfoResponse getCustomerInfo(@AuthenticationPrincipal Long id) {
        return customerService.getInfo(id);
    }
}
