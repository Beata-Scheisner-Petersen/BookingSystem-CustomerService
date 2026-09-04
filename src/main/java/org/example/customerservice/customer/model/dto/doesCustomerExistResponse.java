package org.example.customerservice.customer.model.dto;

import jdk.jfr.BooleanFlag;

public record doesCustomerExistResponse(
        @BooleanFlag
        Boolean customerExist
) {
}
