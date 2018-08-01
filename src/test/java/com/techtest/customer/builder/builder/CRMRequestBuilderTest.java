package com.techtest.customer.builder;

import com.techtest.customer.CRMRequestBuilder;
import com.techtest.customer.CRMRequestBuilderImpl;
import com.techtest.customer.gen.CustomerProfile;
import com.techtest.customer.gen.Request;
import com.techtest.customer.model.Address;
import com.techtest.customer.model.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

public class CRMRequestBuilderTest {

    private CRMRequestBuilder crmRequestBuilder;

    @Before
    public void setUp() {
        crmRequestBuilder = new CRMRequestBuilderImpl();
    }

    @Test
    public void buildCreateCustomerRequest_Success() {
        String customerId = "001";
        String firstName = "Neo";
        String lastName = "Anderson";
        String dob = "2001-01-01";
        String ha = "158, Pitt Street, Sydney";
        String ea = "neo@gmail.com";

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

        CustomerProfile customerProfile = crmRequestBuilder.buildCreateCustomerRequest(customer);

        assertThat(customerProfile.getCustomerIdentificationNumber(), is(customerId));
        assertThat(customerProfile.getFirstName(), is(firstName));
        assertThat(customerProfile.getLastName(), is(lastName));
        assertThat(customerProfile.getDateOfBirth(), is(dob));
        assertThat(customerProfile.getAddresses().getAddress(), hasSize(2));
    }

    @Test
    public void buildReadCustomerProfileRequest_Success() {
        String customerProfileId = "007";
        Request request = crmRequestBuilder.buildReadCustomerProfileRequest(customerProfileId);
        assertThat(request.getId(), is(customerProfileId));
    }
}
