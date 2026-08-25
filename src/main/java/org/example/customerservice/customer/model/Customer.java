package org.example.customerservice.customer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(
        name = "customer"
)
public class Customer {
    @Id
    @Column(
            name = "id",
            unique = true
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @NotBlank(
            message = "You must enter a firstname."
    )
    private String firstname;

    @NotBlank(
            message = "You must enter a lastname."
    )
    private String lastname;

    @Column(
            name = "identification_number",
            unique = true
    )
    @NotBlank(
            message = "You must enter an identification number."
    )
    private String identificationNumber;

    @Column(
            name = "email",
            unique = true
    )
    @NotBlank(
            message = "You must enter a email."
    )
    @Email(
            message = "Email format is invalid."
    )
    private String email;

    @NotBlank(
            message = "You must enter a password."
    )
    private String password;

    @Column(
            name = "phone_number",
            unique = true
    )
    @Pattern(
            regexp = "^(?:\\+46\\s?7\\d-\\d{7}|07\\d-\\d{7}|\\+46\\d{1,3}-\\d{5,8}|0\\d{1,3}-\\d{5,8})$",
            message = "Phone number to be in phone or mobile format, for example xxx-xxxxxxx."
    )
    private String phoneNumber;

    public Customer() {
    }

    public Customer(
            String firstname,
            String lastname,
            String identificationNumber,
            String email,
            String password,
            String phoneNumber
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    //Only used for tests
    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
