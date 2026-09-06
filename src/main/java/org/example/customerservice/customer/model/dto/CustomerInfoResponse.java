package org.example.customerservice.customer.model.dto;

public record CustomerInfoResponse(
        String firstname,
        String lastname,
        String email,
        String phoneNumber
) {
}
