package com.techtest.customer.exp;

public class CustomerDeleteFailed extends CustomerServiceException {

    public CustomerDeleteFailed() {
        super();
    }

    public CustomerDeleteFailed(String message) {
        super(message);
    }
}
