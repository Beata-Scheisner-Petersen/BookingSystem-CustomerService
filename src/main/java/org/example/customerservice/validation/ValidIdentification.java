package org.example.customerservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @Documented makes the annotation visible in Javadoc.
 * @Constraint(validatedBy = IdentificationValidator.class) means When someone uses @ValidIdentification, then run the class IdentificationValidator.
 * @Target({ ElementType.FIELD }) says: this annotation may only be used on fields.
 * @Retention(RetentionPolicy.RUNTIME) says: “The annotation must remain when the program is running.” It is needed for Spring to be able to read it.
 * If IdentificationValidator return false, message() send this as default error message.
 * groups() and payloads() exists there for compatibility with the Bean Validation specification.
 */
@Documented
@Constraint(validatedBy = IdentificationValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentification {

    String message() default "Invalid identification number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
