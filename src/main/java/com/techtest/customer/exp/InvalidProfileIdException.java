package com.techtest.customer.exp;

/**
 * Represent an error scenario where api received an invalid customer profile id.
 */
public class InvalidProfileIdException extends CustomerServiceException {

    public InvalidProfileIdException() {
        super();
    }

    public InvalidProfileIdException(String message) {
        super(message);
    }
}
