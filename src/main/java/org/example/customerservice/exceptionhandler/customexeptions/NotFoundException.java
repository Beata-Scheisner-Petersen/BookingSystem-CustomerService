package org.example.customerservice.exceptionhandler.customexeptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
