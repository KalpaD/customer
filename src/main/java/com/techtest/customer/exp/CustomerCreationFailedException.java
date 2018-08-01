package com.techtest.customer.exp;

/**
 * In case of a failure to create a customer record this exception will be thrown.
 */
public class CustomerCreationFailedException extends  CustomerServiceException {

    public CustomerCreationFailedException() {
        super();
    }

    public CustomerCreationFailedException(String message) {
        super(message);
    }

    public CustomerCreationFailedException(Throwable cause) {
        super(cause);
    }
}
