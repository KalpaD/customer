package com.techtest.customer.exp;

import java.io.IOException;

/**
 * Act at the root exception for all the customer service related exceptions.
 */
public class CustomerServiceException extends IOException {

    public CustomerServiceException() {
        super();
    }

    public CustomerServiceException(String message) {
        super(message);
    }
}
