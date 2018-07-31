package com.techtest.customer.services;

import com.techtest.customer.exp.CustomerAlreadyExistsException;
import com.techtest.customer.exp.CustomerCanNotBeFoundException;
import com.techtest.customer.exp.CustomerCreationFailedException;
import com.techtest.customer.model.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    void createCustomer(Customer customer) throws CustomerAlreadyExistsException, CustomerCreationFailedException;


    Customer getCustomerById(String customerId) throws CustomerCanNotBeFoundException;


    void updateCustomer(Customer customer) throws CustomerCanNotBeFoundException;


    void deleteCustomerById(String customerId) throws CustomerCanNotBeFoundException;

}
