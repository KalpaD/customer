package com.techtest.customer.services;

import com.techtest.customer.builder.CRMRequestBuilder;
import com.techtest.customer.config.EndpointConfig;
import com.techtest.customer.config.HTTPConfig;
import com.techtest.customer.config.RestTemplateConfig;
import com.techtest.customer.customerutil.CustomerUtil;
import com.techtest.customer.exp.*;
import com.techtest.customer.fileutil.FileUtil;
import com.techtest.customer.model.Customer;
import com.techtest.customer.util.HeaderUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static com.techtest.customer.cons.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

@RunWith(SpringRunner.class)
@RestClientTest({CustomerService.class,
        RestTemplateConfig.class, HTTPConfig.class,
        CRMRequestBuilder.class, EndpointConfig.class, HeaderUtil.class})
public class CustomerServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CustomerServiceImpl customerService;

    private FileUtil fileUtil = new FileUtil();

    private static final String customerId = "001";
    private static final String firstName = "Neo";
    private static final String lastName = "Anderson";
    private static final String dob = "2001-01-01";
    private static final String ha = "158, Pitt Street, Sydney";
    private static final String ea = "neo@gmail.com";

    @Before
    public void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testCreateCustomer_200() throws CustomerAlreadyExistsException, CustomerCreationFailedException {
        String responseAsString = fileUtil.getStringFromFile(CRM_CREATE_CUSTOMER_PROFILE_SUCCESS);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/createCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withSuccess(responseAsString, MediaType.APPLICATION_XML));

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
        customerService.createCustomer(customer);
        mockRestServiceServer.verify();
    }

    @Test
    public void testCreateCustomer_500_CustomerExists() throws CustomerAlreadyExistsException, CustomerCreationFailedException {
        exception.expectCause(isA(CustomerAlreadyExistsException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM001);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/createCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
        customerService.createCustomer(customer);
        mockRestServiceServer.verify();
    }

    @Test
    public void testCreateCustomer_500_CustomerCreationFailed() throws CustomerAlreadyExistsException, CustomerCreationFailedException {
        exception.expectCause(isA(CustomerCreationFailedException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM002);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/createCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
        customerService.createCustomer(customer);
        mockRestServiceServer.verify();
    }

    @Test
    public void testReadCustomer_200() throws CustomerCanNotBeFoundException, CustomerReadFailedException {
        String responseAsString = fileUtil.getStringFromFile(CRM_READ_CUSTOMER_PROFILE_SUCCESS);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/readCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withSuccess(responseAsString, MediaType.APPLICATION_XML));

        Customer customerById = customerService.getCustomerById("001");

        assertThat(customerById.getCustomerId(), is(customerId));
        assertThat(customerById.getFirstName(), is(firstName));
        assertThat(customerById.getLastName(), is(lastName));
        assertThat(customerById.getDateOfBirth(), is(dob));
        assertThat(customerById.getAddressList(), hasSize(1));

        mockRestServiceServer.verify();
    }

    @Test
    public void testReadCustomer_500_CustomerNotFound() throws CustomerCanNotBeFoundException, CustomerReadFailedException {
        exception.expectCause(isA(CustomerCanNotBeFoundException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM003);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/readCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        customerService.getCustomerById("001");
        mockRestServiceServer.verify();
    }

    @Test
    public void testUpdateCustomer_200() throws CustomerUpdateFailedException, CustomerCanNotBeFoundException {
        String responseAsString = fileUtil.getStringFromFile(CRM_UPDATE_CUSTOMER_PROFILE_SUCCESS);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/updateCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withSuccess(responseAsString, MediaType.APPLICATION_XML));

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
        customerService.updateCustomer(customer);
        mockRestServiceServer.verify();
    }

    @Test
    public void testUpdateCustomer_500_CustomerNotFound() throws CustomerUpdateFailedException, CustomerCanNotBeFoundException {
        exception.expectCause(isA(CustomerCanNotBeFoundException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM003);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/updateCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
        customerService.updateCustomer(customer);
        mockRestServiceServer.verify();
    }

    @Test
    public void testUpdateCustomer_500_CustomerUpdateFailed() throws CustomerUpdateFailedException, CustomerCanNotBeFoundException {
        exception.expectCause(isA(CustomerUpdateFailedException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM004);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/updateCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        Customer customer = CustomerUtil.buildCustomer(customerId, firstName, lastName, dob, ha, ea);
        customerService.updateCustomer(customer);
        mockRestServiceServer.verify();
    }

    @Test
    public void testDeleteCustomer_200() throws CustomerDeleteFailedException, CustomerCanNotBeFoundException {
        String responseAsString = fileUtil.getStringFromFile(CRM_DELETE_CUSTOMER_PROFILE_SUCCESS);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/deleteCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withSuccess(responseAsString, MediaType.APPLICATION_XML));

        customerService.deleteCustomerById("001");
        mockRestServiceServer.verify();
    }

    @Test
    public void testDeleteCustomer_500_CustomerNotFound() throws CustomerDeleteFailedException, CustomerCanNotBeFoundException {
        exception.expectCause(isA(CustomerCanNotBeFoundException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM003);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/deleteCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        customerService.deleteCustomerById("001");
        mockRestServiceServer.verify();
    }

    @Test
    public void testDeleteCustomer_500_DeleteFailed() throws CustomerDeleteFailedException, CustomerCanNotBeFoundException {
        exception.expectCause(isA(CustomerDeleteFailedException.class));
        String responseAsString = fileUtil.getStringFromFile(CRM005);
        mockRestServiceServer
                .expect(requestTo("http://localhost:8089/deleteCustomerProfile"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/xml"))
                .andRespond(MockRestResponseCreators.withServerError()
                        .body(responseAsString).contentType(MediaType.APPLICATION_XML));

        customerService.deleteCustomerById("001");
        mockRestServiceServer.verify();
    }

}
