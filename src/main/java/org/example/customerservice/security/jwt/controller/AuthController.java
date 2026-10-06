package org.example.customerservice.security.jwt.controller;

import org.example.customerservice.customer.model.Customer;
import org.example.customerservice.customer.model.dto.CustomerLoginRequest;
import org.example.customerservice.customer.service.CustomerService;
import org.example.customerservice.exceptionhandler.customexeptions.InvalidCredentialsException;
import org.example.customerservice.security.jwt.service.JwtService;
import org.example.customerservice.security.password.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwt;
    private final CustomerService service;
    private final PasswordService bcrypt;
    final Logger logger = LoggerFactory.getLogger(AuthController.class);

    AuthController(JwtService jwt,  CustomerService service, PasswordService bcrypt) {
        this.jwt = jwt;
        this.service = service;
        this.bcrypt = bcrypt;
    }

    @PostMapping("/login")
    public String login(@RequestBody CustomerLoginRequest request){

        Customer customer = service.getCustomerInformation(request.email());

        if(bcrypt.matches(request.password(), customer.getPassword())) {
            return jwt.generateToken(customer.getId());
        }
        logger.error("Invalid email or password logg");
        throw new InvalidCredentialsException("Invalid email or password.");
    }
}
