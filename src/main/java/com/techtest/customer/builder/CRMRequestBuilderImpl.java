package com.techtest.customer.builder;

import com.techtest.customer.gen.CustomerProfile;
import com.techtest.customer.gen.ObjectFactory;
import com.techtest.customer.gen.Request;
import com.techtest.customer.model.Address;
import com.techtest.customer.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Provides the implementation of the customer profile POX requests builds.
 */
@Slf4j
@Component
public class CRMRequestBuilderImpl implements CRMRequestBuilder {

    @Override
    public CustomerProfile buildCreateCustomerRequest(Customer customer) {
        return createCustomerRequest(customer);
    }

    @Override
    public CustomerProfile buildUpdateCustomerRequest(Customer customer) {
        return createCustomerRequest(customer);
    }

    @Override
    public Request buildReadCustomerProfileRequest(String customerProfileId) {
        return createCustomerProfileIdRequest(customerProfileId);
    }

    @Override
    public Request buildDeleteCustomerProfileRequest(String customerProfileId) {
        return createCustomerProfileIdRequest(customerProfileId);
    }

    /**
     * Build a CRM customer profile request for a given customer object.
     *
     * @param customer The customer object
     * @return  A CRM {@link CustomerProfile} object.
     */
    private CustomerProfile createCustomerRequest(Customer customer) {
        ObjectFactory objectFactory = new ObjectFactory();
        CustomerProfile  customerProfile = objectFactory.createCustomerProfile();
        customerProfile.setCustomerIdentificationNumber(customer.getCustomerId());
        customerProfile.setFirstName(customer.getFirstName());
        customerProfile.setLastName(customer.getLastName());
        customerProfile.setDateOfBirth(customer.getDateOfBirth());

        CustomerProfile.Addresses customerProfileAddresses = objectFactory.createCustomerProfileAddresses();
        for (Address add : customer.getAddressList()) {
            CustomerProfile.Addresses.Address address = objectFactory.createCustomerProfileAddressesAddress();
            address.setAddress(add.getAddressType().toString());
            address.setAddress(add.getAddress());
            customerProfileAddresses.getAddress().add(address);
        }
        customerProfile.setAddresses(customerProfileAddresses);
        return customerProfile;
    }

    /**
     * Build a CRM customer profile id request for a given customer id.
     *
     * @param id The customer profile id.
     * @return  A JAXB object representation of the customer profile id.
     */
    private Request createCustomerProfileIdRequest(String id) {
        ObjectFactory objectFactory = new ObjectFactory();
        Request request = objectFactory.createRequest();
        request.setId(id);
        return request;
    }

}
