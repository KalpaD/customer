package com.techtest.customer.exp;

public class CustomerUpdateFailedException extends CustomerServiceException {

    public CustomerUpdateFailedException() {
        super();
    }

    public CustomerUpdateFailedException(String message) {
        super(message);
    }
}
