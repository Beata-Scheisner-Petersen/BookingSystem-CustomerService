package org.example.customerservice.customer.model.dto;

public record ReservationStatusRequest(
        boolean hasActiveReservations
) {
}