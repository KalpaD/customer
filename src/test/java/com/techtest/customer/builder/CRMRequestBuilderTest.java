package com.techtest.customer.builder;

import com.techtest.customer.customerutil.CustomerUtil;
import com.techtest.customer.gen.CustomerProfile;
import com.techtest.customer.gen.Request;
import com.techtest.customer.model.Customer;
import org.junit.Before;
import org.junit.Test;

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

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
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
