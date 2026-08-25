package org.example.customerservice.exceptionhandler.customexeptions;

public class HaveReservationException extends RuntimeException {
    public HaveReservationException(String message){
        super(message);
    }
}
