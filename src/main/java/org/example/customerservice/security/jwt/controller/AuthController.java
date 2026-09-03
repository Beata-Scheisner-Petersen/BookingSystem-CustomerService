package org.example.customerservice.security.jwt.controller;

import org.example.customerservice.customer.model.Customer;
import org.example.customerservice.customer.service.CustomerService;
import org.example.customerservice.security.jwt.service.JwtService;
import org.example.customerservice.security.password.PasswordService;
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

    AuthController(JwtService jwt,  CustomerService service, PasswordService bcrypt) {
        this.jwt = jwt;
        this.service = service;
        this.bcrypt = bcrypt;
    }

    record LoginDto(String email, String password){}

    @PostMapping("/login")
    public String login(@RequestBody LoginDto dto){

        Customer customer = service.getCustomerInformation(dto.email);

        if(bcrypt.matches(dto.password, customer.getPassword())) {
            return jwt.generateToken(customer.getId());
        }
        throw new RuntimeException("Fel inloggning");
    }
}
