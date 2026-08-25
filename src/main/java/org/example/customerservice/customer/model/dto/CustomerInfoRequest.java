package org.example.customerservice.customer.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerInfoRequest(
        @NotBlank(
                message = "You must enter a first name."
        )
        String firstname,

        @NotBlank(
                message = "You must enter a last name."
        )
        String lastname,

        @NotBlank(
                message = "You must enter an email."
        )
        @Email(
                message = "Email format is invalid."
        )
        String email,

        @NotBlank(
                message = "You must enter a phone number."
        )
        @Pattern(
                regexp = "^(?:\\+46\\s?7\\d-\\d{7}|07\\d-\\d{7}|\\+46\\d{1,3}-\\d{5,8}|0\\d{1,3}-\\d{5,8})$",
                message = "Phone number to be in phone or mobile format, for example xxx-xxxxxxx."
        )
        String phoneNumber
) {
}
