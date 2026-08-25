package org.example.customerservice.exceptionhandler;

import org.example.customerservice.exceptionhandler.customexeptions.AlreadyExistException;
import org.example.customerservice.exceptionhandler.customexeptions.HaveReservationException;
import org.example.customerservice.exceptionhandler.customexeptions.WrongEmailOrPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler = handles everything else
 * Business Error
 * Validation Error
 * Database Error
 * Controller Error
 * JWT error never makes it here.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().
                getFieldErrors()
                .forEach(
                        error -> errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleUsernameExists(AlreadyExistException e) {
        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        e.getMessage()
                );
    }

    @ExceptionHandler(WrongEmailOrPasswordException.class)
    public ResponseEntity<String> handleWrongEmailOrPassword(WrongEmailOrPasswordException e) {
        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        e.getMessage()
                );
    }

    @ExceptionHandler(HaveReservationException.class)
    public ResponseEntity<String> HaveReservation(HaveReservationException e) {
        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        e.getMessage()
                );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(
            IllegalArgumentException e) {

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                ).body(
                        e.getMessage()
                );
    }

}
