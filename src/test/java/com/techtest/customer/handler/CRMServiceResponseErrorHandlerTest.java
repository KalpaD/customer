package com.techtest.customer.handler;

import com.techtest.customer.exp.*;
import com.techtest.customer.fileutil.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static com.techtest.customer.cons.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class CRMServiceResponseErrorHandlerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CRMServiceResponseErrorHandler errorHandler;
    private ClientHttpResponse clientHttpResponse;

    private FileUtil fileUtil;

    @Before
    public void setUp() {
        errorHandler = new CRMServiceResponseErrorHandler();
        clientHttpResponse = mock(ClientHttpResponse.class);

        fileUtil = new FileUtil();
    }

    @Test
    public void testHasError_200() throws IOException {
        when(clientHttpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        boolean hasError = errorHandler.hasError(clientHttpResponse);
        assertThat(hasError, is(false));
    }

    @Test
    public void testHasError_500() throws IOException {
        when(clientHttpResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        boolean hasError = errorHandler.hasError(clientHttpResponse);
        assertThat(hasError, is(true));
    }

    @Test
    public void testHandleError_CRM001() throws IOException {
        exception.expect(CustomerAlreadyExistsException.class);
        InputStream inputStream = fileUtil.getInputStreamFromFile(CRM001);
        when(clientHttpResponse.getBody()).thenReturn(inputStream);
        errorHandler.handleError(clientHttpResponse);
    }

    @Test
    public void testHandleError_CRM002() throws IOException {
        exception.expect(CustomerCreationFailedException.class);
        InputStream inputStream = fileUtil.getInputStreamFromFile(CRM002);
        when(clientHttpResponse.getBody()).thenReturn(inputStream);
        errorHandler.handleError(clientHttpResponse);
    }

    @Test
    public void testHandleError_CRM003() throws IOException {
        exception.expect(CustomerCanNotBeFoundException.class);
        InputStream inputStream = fileUtil.getInputStreamFromFile(CRM003);
        when(clientHttpResponse.getBody()).thenReturn(inputStream);
        errorHandler.handleError(clientHttpResponse);
    }

    @Test
    public void testHandleError_CRM004() throws IOException {
        exception.expect(CustomerUpdateFailedException.class);
        InputStream inputStream = fileUtil.getInputStreamFromFile(CRM004);
        when(clientHttpResponse.getBody()).thenReturn(inputStream);
        errorHandler.handleError(clientHttpResponse);
    }

    @Test
    public void testHandleError_CRM005() throws IOException {
        exception.expect(CustomerDeleteFailedException.class);
        InputStream inputStream = fileUtil.getInputStreamFromFile(CRM005);
        when(clientHttpResponse.getBody()).thenReturn(inputStream);
        errorHandler.handleError(clientHttpResponse);
    }

    @Test
    public void testUnknownError() throws IOException {
        exception.expect(CustomerServiceException.class);
        exception.expectMessage("Unknown error occurred while invoking the CRM endpoint.");
        InputStream inputStream = fileUtil.getInputStreamFromFile(CRM006);
        when(clientHttpResponse.getBody()).thenReturn(inputStream);
        errorHandler.handleError(clientHttpResponse);
    }

    @Test
    public void testMessageReadFailure() throws IOException {
        exception.expect(CustomerServiceException.class);
        exception.expectMessage("Message read error occurred while invoking the CRM endpoint.");
        when(clientHttpResponse.getBody()).thenReturn(null);
        errorHandler.handleError(clientHttpResponse);
    }
 }
