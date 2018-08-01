package com.techtest.customer.exp;

/**
 * In case of customer profile update failure.
 */
public class CustomerUpdateFailedException extends CustomerServiceException {

    public CustomerUpdateFailedException() {
        super();
    }

    public CustomerUpdateFailedException(String message) {
        super(message);
    }
}
