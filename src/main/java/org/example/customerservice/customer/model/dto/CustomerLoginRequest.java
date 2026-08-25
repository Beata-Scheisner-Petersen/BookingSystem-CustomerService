package org.example.customerservice.customer.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerLoginRequest(
        @NotBlank(
                message = "You must enter an email."
        )
        @Email(
                message = "Not a valid email."
        )
        String email,

        @NotBlank(
                message = "You must enter a password."
        )
        @Size(
                min = 10,
                message = "Password needs to be at least 10 character long"
        )
        @Pattern(
                regexp = ".*[^a-zA-Z0-9].*",
                message = "Password have to contain at least one special character"
        )
        @Pattern(
                regexp = ".*[A-Z].*",
                message = "Password have to contain at least one uppercase letter"
        )
        @Pattern(
                regexp = ".*[a-z].*",
                message = "Password have to contain at least one lowercase letter"
        )
        @Pattern(
                regexp = ".*[0-9].*",
                message = "Password have to contain at least one number"
        )
        String password) {
}
