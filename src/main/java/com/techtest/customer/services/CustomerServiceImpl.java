package com.techtest.customer.services;

import com.techtest.customer.builder.CRMRequestBuilder;
import com.techtest.customer.config.EndpointConfig;
import com.techtest.customer.exp.CustomerCreationFailedException;
import com.techtest.customer.gen.CustomerProfile;
import com.techtest.customer.gen.Request;
import com.techtest.customer.gen.Response;
import com.techtest.customer.model.Address;
import com.techtest.customer.model.Customer;
import com.techtest.customer.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * The business logic implementation of the customer service where it consumes services from
 * CRM back end and expose modified data to controller layer.
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private RestTemplate restTemplate;
    private CRMRequestBuilder crmRequestBuilder;
    private EndpointConfig endpointConfig;
    private HeaderUtil headerUtil;

    public CustomerServiceImpl(RestTemplate restTemplate, CRMRequestBuilder crmRequestBuilder,
                               EndpointConfig endpointConfig, HeaderUtil headerUtil) {
        this.restTemplate = restTemplate;
        this.crmRequestBuilder = crmRequestBuilder;
        this.endpointConfig = endpointConfig;
        this.headerUtil = headerUtil;
    }

    /**
     * Create a customer with given customer information.
     * @param customer                          The customer profile information.
     * @throws CustomerCreationFailedException  If customer record creation failed due to system error.
     */
    @Override
    public void createCustomer(Customer customer) throws CustomerCreationFailedException {
        log.info("Creating customer : {}", customer);

        // build the CRM request
        CustomerProfile customerProfile = crmRequestBuilder.buildCreateCustomerRequest(customer);

        HttpEntity<CustomerProfile> request = new HttpEntity<>(customerProfile, headerUtil.createHeaderForCRMEndpopint());

        restTemplate.exchange(endpointConfig.getCrmCreateCustomerEndpoint(), HttpMethod.POST, request, Response.class);

        log.info("Customer created successfully with id : {}", customer.getCustomerId());
    }

    /**
     * Update customer profile with given profile information.
     * @param customer  The customer profile information.
     */
    @Override
    public void updateCustomer(Customer customer) {
        log.info("Updating customer : {}", customer);

        // build the CRM request
        CustomerProfile customerProfile = crmRequestBuilder.buildUpdateCustomerRequest(customer);

        HttpEntity<CustomerProfile> request = new HttpEntity<>(customerProfile, headerUtil.createHeaderForCRMEndpopint());

        restTemplate.exchange(endpointConfig.getCrmUpdateCustomerEndpoint(), HttpMethod.POST, request, Response.class);

        log.info("Customer with id : {} successfully updated.", customer.getCustomerId());
    }

    /**
     * Delete customer profile with given customer id.
     * @param customerId    Customer profile id to be deleted.
     */
    @Override
    public void deleteCustomerById(String customerId) {
        log.info("Deleting customer with customer id: {}", customerId);

        Request id = crmRequestBuilder.buildDeleteCustomerProfileRequest(customerId);

        HttpEntity<Request> request = new HttpEntity<>(id, headerUtil.createHeaderForCRMEndpopint());

        restTemplate.exchange(endpointConfig.getCrmDeleteCustomerEndpoint(), HttpMethod.POST, request, Response.class);

        log.info("Customer with id : {} successfully deleted.", customerId);
    }

    /**
     * Fetch customer profile from back end and transform in to customer API's own format.
     * @param customerId    Customer profile id to be fetched.
     * @return              A customer profile identified by the given id.
     */
    @Override
    public Customer getCustomerById(String customerId) {
        log.info("Fetching customer profile with id: {}", customerId);

        Request id = crmRequestBuilder.buildReadCustomerProfileRequest(customerId);

        HttpEntity<Request> request = new HttpEntity<>(id, headerUtil.createHeaderForCRMEndpopint());

        ResponseEntity<CustomerProfile> profileEntity=
                restTemplate.exchange(endpointConfig.getCrmReadCustomerEndpoint(),
                        HttpMethod.POST, request, CustomerProfile.class);

        CustomerProfile profile = profileEntity.getBody();

        Customer customer = new Customer();
        customer.setCustomerId(profile.getCustomerIdentificationNumber().toString());
        customer.setFirstName(profile.getFirstName().toString());
        customer.setLastName(profile.getLastName().toString());
        customer.setDateOfBirth(profile.getDateOfBirth().toString());

        if (profile.getAddresses() != null) {
            List<Address> list = new ArrayList<>();
            List<CustomerProfile.Addresses.Address> address = profile.getAddresses().getAddress();

            for (CustomerProfile.Addresses.Address ad : address) {
                Address newAddress = new Address();
                newAddress.setAddressType(Address.ADDRESS_TYPE.valueOf((String) ad.getAddressType().toString()));
                newAddress.setAddress((String) ad.getAddress());
                list.add(newAddress);
            }
            customer.setAddressList(list);
        }

        // we can load any other non mandatory property
        //customer.setInfo(propertyName, propertyValue);
        return customer;
    }
}
