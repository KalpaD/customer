package com.techtest.customer.exp;

public class CustomerReadFailedException extends CustomerServiceException {

    public CustomerReadFailedException() {
        super();
    }

    public CustomerReadFailedException(String message) {
        super(message);
    }
}
