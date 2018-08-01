package com.techtest.customer.exp;

/**
 *  In cae of customer record deletion failure.
 */
public class CustomerDeleteFailedException extends CustomerServiceException {

    public CustomerDeleteFailedException() {
        super();
    }

    public CustomerDeleteFailedException(String message) {
        super(message);
    }
}
