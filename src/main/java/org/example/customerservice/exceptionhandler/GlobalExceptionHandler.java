package org.example.customerservice.exceptionhandler;

import org.example.customerservice.exceptionhandler.customexeptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        var response = ResponseEntity
                .badRequest()
                .body(errors);
        printLoggingWarning(
                response,
                "handleValidationError"
        );
        return response;
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleUsernameExists(AlreadyExistException exception) {
        var response = ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        exception.getMessage()
                );
        printLoggingWarning(
                response,
                "handleUsernameExists"
        );
        return response;
    }

    @ExceptionHandler(WrongEmailOrPasswordException.class)
    public ResponseEntity<String> handleWrongEmailOrPassword(WrongEmailOrPasswordException exception) {
        var response = ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        exception.getMessage()
                );
        printLoggingWarning(
                response,
                "handleWrongEmailOrPassword"
        );
        return response;
    }

    @ExceptionHandler(HaveReservationException.class)
    public ResponseEntity<String> haveReservation(HaveReservationException exception) {
        var response = ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                ).body(
                        exception.getMessage()
                );
        printLoggingWarning(
                response,
                "haveReservation"
        );
        return response;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception) {
        var response = ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                ).body(
                        exception.getMessage()
                );
        printLoggingWarning(
                response,
                "handleIllegalArgument"
        );
        return response;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException exception) {
        var response = ResponseEntity
                .status(
                        HttpStatus.UNAUTHORIZED
                ).body(
                        exception.getMessage()
                );
        printLoggingWarning(
                response,
                "handleInvalidCredentials"
        );
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException exception) {
        var response = ResponseEntity
                .status(
                        HttpStatus.NOT_FOUND
                ).body(
                        exception.getMessage()
                );
        printLoggingWarning(
                response,
                "handleNotFound"
        );
        return response;
    }

    private void printLoggingWarning(ResponseEntity<?> response, String className) {
        logger.warn("""
                GlobalExceptionHandler: {}
                Status: {}
                Message: {}
                """, className ,response.getStatusCode(), response.getBody()
        );
    }

}
