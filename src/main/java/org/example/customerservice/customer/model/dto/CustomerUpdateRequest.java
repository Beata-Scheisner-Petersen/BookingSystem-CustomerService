package org.example.customerservice.customer.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
        String firstname,

        String lastname,

        @Email(
                message = "Email format is invalid."
        )
        String email,

        @Pattern(
                regexp = "^(?:\\+46\\s?7\\d-\\d{7}|07\\d-\\d{7}|\\+46\\d{1,3}-\\d{5,8}|0\\d{1,3}-\\d{5,8})$",
                message = "Phone number to be in phone or mobile format, for example xxx-xxxxxxx."
        )
        String phoneNumber,

        @Size(
                min = 10
        )
        @Pattern(
                regexp = ".*[^a-zA-Z0-9].*",
                message = "Password have to contain a least one special character"
        )
        @Pattern(
                regexp = ".*[A-Z].*",
                message = "Password have to contain a least one uppercase letter"
        )
        @Pattern(
                regexp = ".*[a-z].*",
                message = "Password have to contain a least one lowercase letter"
        )
        @Pattern(
                regexp = ".*[0-9].*",
                message = "Password have to contain a least one number"
        )
        String password
) {
}
