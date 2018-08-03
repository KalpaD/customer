package com.techtest.customer.api.controllers;

import com.techtest.customer.api.AbstractControllerTest;
import com.techtest.customer.controllers.CustomerServiceController;
import com.techtest.customer.exp.*;
import com.techtest.customer.fileutil.FileUtil;
import com.techtest.customer.model.Customer;
import com.techtest.customer.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.techtest.customer.cons.TestConstants.POST_CUSTOMER;
import static com.techtest.customer.cons.TestConstants.PUT_CUSTOMER;
import static com.techtest.customer.cons.TestConstants.READ_CUSTOMER_SUCCESS;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CustomerServiceControllerTest extends AbstractControllerTest {

    private static final String CUSTOMER_PATH = "/customers";
    private static final String CUSTOMER_ID_PATH = "/customers/{id}";

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerServiceController controller;

    private FileUtil fileUtil = new FileUtil();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setUp(controller);
    }

    @Test
    public void testCreateCustomer_201_Created() throws Exception {
        String requestAsString = fileUtil.getStringFromFile(POST_CUSTOMER);
        doNothing().when(customerService).createCustomer(any(Customer.class));
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .post(CUSTOMER_PATH).content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(201));
        verify(customerService, times(1)).createCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testCreateCustomer_409_Conflict() throws Exception {
        String requestAsString = fileUtil.getStringFromFile(POST_CUSTOMER);
        doThrow(CustomerAlreadyExistsException.class)
                .when(customerService).createCustomer(any(Customer.class));
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .post(CUSTOMER_PATH).content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerRecordExists")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(409));
        verify(customerService, times(1))
                .createCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testCreateCustomer_500_Internal_Server_Error() throws Exception {
        String requestAsString = fileUtil.getStringFromFile(POST_CUSTOMER);
        doThrow(CustomerCreationFailedException.class)
                .when(customerService).createCustomer(any(Customer.class));
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .post(CUSTOMER_PATH).content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerRecordCreationFailed")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(500));
        verify(customerService, times(1))
                .createCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testReadCustomer_200_OK() throws Exception {
        String responseAsString = fileUtil.getStringFromFile(READ_CUSTOMER_SUCCESS);
        Customer customer = mapFromJson(responseAsString, Customer.class);
        when(customerService.getCustomerById(anyString())).thenReturn(customer);

        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.
                        get(CUSTOMER_ID_PATH, "1234"))
                        .andExpect(jsonPath("$.customerId", is("1234")))
                        .andExpect(jsonPath("$.firstName", is("Jhon")))
                        .andExpect(jsonPath("$.lastName", is("Doe")))
                        .andExpect(jsonPath("$.dateOfBirth", is("1990-01-01")))
                        .andExpect(jsonPath("$.addressList", hasSize(1)))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(200));
        verify(customerService, times(1))
                .getCustomerById(anyString());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testReadCustomer_404_NotFound() throws Exception {
        doThrow(CustomerCanNotBeFoundException.class)
                .when(customerService).getCustomerById(anyString());
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.
                        get(CUSTOMER_ID_PATH, "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerCanNotBeFound")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(404));
        verify(customerService, times(1))
                .getCustomerById(anyString());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testReadCustomer_500_Internal_Server_Error() throws Exception {
        doThrow(CustomerReadFailedException.class)
                .when(customerService).getCustomerById(anyString());
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.
                        get(CUSTOMER_ID_PATH, "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerReadFailedException")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(500));
        verify(customerService, times(1))
                .getCustomerById(anyString());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testUpdateCustomer_204_Created() throws Exception {
        String requestAsString = fileUtil.getStringFromFile(PUT_CUSTOMER);
        doNothing().when(customerService).updateCustomer(any(Customer.class));
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .put(CUSTOMER_PATH).content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(204));
        verify(customerService, times(1)).updateCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testUpdateCustomer_404_NotFound() throws Exception {
        String requestAsString = fileUtil.getStringFromFile(POST_CUSTOMER);
        doThrow(CustomerCanNotBeFoundException.class)
                .when(customerService).updateCustomer(any(Customer.class));
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .put(CUSTOMER_PATH).content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerCanNotBeFound")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(404));
        verify(customerService, times(1))
                .updateCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testUpdateCustomer_500_Internal_Server_Error() throws Exception {
        String requestAsString = fileUtil.getStringFromFile(POST_CUSTOMER);
        doThrow(CustomerUpdateFailedException.class)
                .when(customerService).updateCustomer(any(Customer.class));
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .put(CUSTOMER_PATH).content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerUpdateFailed")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(500));
        verify(customerService, times(1))
                .updateCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testDeleteCustomer_204_OK() throws Exception {
        doNothing().when(customerService).deleteCustomerById(anyString());
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.
                        delete(CUSTOMER_ID_PATH, "1234"))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(204));
        verify(customerService, times(1))
                .deleteCustomerById(anyString());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testDeleteCustomer_404_NotFound() throws Exception {
        doThrow(CustomerCanNotBeFoundException.class)
                .when(customerService).deleteCustomerById(anyString());
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.
                        delete(CUSTOMER_ID_PATH, "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerCanNotBeFound")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(404));
        verify(customerService, times(1))
                .deleteCustomerById(anyString());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testDeleteCustomer_500_Internal_Server_Error() throws Exception {
        doThrow(CustomerDeleteFailedException.class)
                .when(customerService).deleteCustomerById(anyString());
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.
                        delete(CUSTOMER_ID_PATH, "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errorMessage", is("CustomerDeleteFailedException")))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status, is(500));
        verify(customerService, times(1))
                .deleteCustomerById(anyString());
        verifyNoMoreInteractions(customerService);
    }
}
