package com.techtest.customer.exp;

/**
 * If the customer can not be found in system for a given operation to be invoked.
 */
public class CustomerCanNotBeFoundException extends CustomerServiceException {

    public CustomerCanNotBeFoundException() {
        super();
    }

    public CustomerCanNotBeFoundException(String message) {
        super(message);
    }
}
