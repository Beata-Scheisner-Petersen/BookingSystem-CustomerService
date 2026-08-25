package org.example.customerservice.exceptionhandler.customexeptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
