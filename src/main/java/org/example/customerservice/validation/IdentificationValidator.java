package org.example.customerservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
/**
 * This is a custom validator that is connected to an annotation @ValidIdentification.
 * ConstraintValidator\<ValidIdentification, String\>: Says: "I validate fields that are annotated with @ValidIdentification and that are of type String."
 * ConstraintValidatorContext is a tool from Jakarta validation and has to be there as it is a requirement from the framework.
 * Check if format is valid
 * datePart take out the date part of the identification number.
 * Convert 6 number to 8 by adding either 19 or 20 in the start.
 * Makes a strict formatting, so there will be no magical corrections.
 */
public class IdentificationValidator implements ConstraintValidator<ValidIdentification, String> {

    @Override
    public boolean isValid(
            String identificationNumber,
            ConstraintValidatorContext context
    ) {

        if (identificationNumber == null || identificationNumber.isBlank()) {
            return false;
        }

        identificationNumber = identificationNumber.trim();


        if (!identificationNumber.matches("^(\\d{6}|\\d{8})-\\d{4}$")) {
            return false;
        }

        String datePart = identificationNumber.split("-")[0];

        if (datePart.length() == 6) {
            String year = datePart.substring(0, 2);
            int yearInt = Integer.parseInt(year);

            datePart = (yearInt <= 24 ? "20" : "19") + datePart;
        }

        // Validate date (STRICT mode)
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("uuuuMMdd")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(datePart, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
