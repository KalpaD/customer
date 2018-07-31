package com.techtest.customer.builder;

import com.techtest.customer.gen.CustomerProfile;
import com.techtest.customer.gen.Request;
import com.techtest.customer.model.Customer;

/**
 * This interface provides the contract to build POX requests towards existing CRM endpoints.
 */
public interface CRMRequestBuilder {

    /**
     * Builds create customer profile POX request.
     * @param customer  The customer profile information.
     * @return          Built CustomerProfile object.
     */
    CustomerProfile buildCreateCustomerRequest(Customer customer);

    /**
     * Builds update customer profile  POX request.
     * @param customer  The customer profile information.
     * @return           Built CustomerProfile object.
     */
    CustomerProfile buildUpdateCustomerRequest(Customer customer);

    /**
     * Builds read customer profile POX request.
     * @param customerProfileId Customer profile id.
     * @return                  Customer profile id request.
     */
    Request buildReadCustomerProfileRequest(String customerProfileId);

    /**
     * Builds delete customer profile POX request.
     * @param customerProfileId Customer profile id.
     * @return                  Customer profile id request.
     */
    Request buildDeleteCustomerProfileRequest(String customerProfileId);

}
