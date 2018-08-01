package com.techtest.customer.customerutil;

import com.techtest.customer.model.Address;
import com.techtest.customer.model.Customer;

import java.util.Arrays;
import java.util.List;

public class CustomerUtil {

    public static Customer buildCustomer(String customerId, String firstName, String lastName,
                                         String dob, String ha, String ea) {

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(dob);

        Address homeAddress = new Address();
        homeAddress.setAddressType(Address.ADDRESS_TYPE.HOME);
        homeAddress.setAddress(ha);

        Address email = new Address();
        email.setAddressType(Address.ADDRESS_TYPE.EMAIL);
        email.setAddress(ea);

        List<Address> addressList = Arrays.asList(homeAddress, email);
        customer.setAddressList(addressList);

        return customer;
    }
}
