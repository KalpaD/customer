package com.techtest.customer.exp;

/**
 * If customer already exists in the CRM this application will throw this exception.
 */
public class CustomerAlreadyExistsException extends CustomerServiceException {

    public CustomerAlreadyExistsException() {
        super();
    }

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
