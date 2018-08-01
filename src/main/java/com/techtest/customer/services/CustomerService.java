package com.techtest.customer.services;

import com.techtest.customer.exp.*;
import com.techtest.customer.model.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    void createCustomer(Customer customer) throws CustomerAlreadyExistsException, CustomerCreationFailedException;


    Customer getCustomerById(String customerId) throws CustomerCanNotBeFoundException, CustomerReadFailedException;


    void updateCustomer(Customer customer) throws CustomerCanNotBeFoundException, CustomerUpdateFailedException;


    void deleteCustomerById(String customerId) throws CustomerCanNotBeFoundException, CustomerDeleteFailedException;

}
