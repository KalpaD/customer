package com.techtest.customer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String errorMessage;
    private String details;

    public ErrorResponse(String message, String details) {
        this.errorMessage = message;
        this.details = details;
    }
}
