package com.techtest.customer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Holds the address information of the customer record.
 */
@Getter
@Setter
@ToString
public class Address {

    public enum ADDRESS_TYPE {
        HOME, OFFICE, EMAIL
    }

    private ADDRESS_TYPE addressType;
    private String address;
}
