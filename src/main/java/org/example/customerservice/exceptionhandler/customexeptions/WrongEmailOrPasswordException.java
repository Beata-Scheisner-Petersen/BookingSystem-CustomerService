package org.example.customerservice.exceptionhandler.customexeptions;

public class WrongEmailOrPasswordException extends RuntimeException{
    public WrongEmailOrPasswordException(String message) {
        super(message);
    }
}
