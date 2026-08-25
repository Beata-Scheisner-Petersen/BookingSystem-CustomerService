package org.example.customerservice.customer.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.customerservice.validation.ValidIdentification;

public record CreateCustomerRequest(
        @NotBlank(
                message = "You must enter a first name."
        )
        String firstname,

        @NotBlank(
                message = "You must enter a last name."
        )
        String lastname,

        @NotBlank(
                message = "You must enter an identification number."
        )
        @ValidIdentification
        String identificationNumber,

        @NotBlank(
                message = "You must enter an email."
        )
        @Email(
                message = "Email format is invalid."
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
        String password,

        @NotBlank(
                message = "You must enter a phone number."
        )
        @Pattern(
                regexp = "^(?:\\+46\\s?7\\d-\\d{7}|07\\d-\\d{7}|\\+46\\d{1,3}-\\d{5,8}|0\\d{1,3}-\\d{5,8})$",
                message = "Phone number to be in phone or mobile format, for example xxx-xxxxxxx."
        )
        String phoneNumber) {
}
