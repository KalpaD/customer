package com.techtest.customer.handler;

import com.techtest.customer.cons.Constants;
import com.techtest.customer.exp.*;
import com.techtest.customer.gen.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CRMServiceResponseErrorHandler implements ResponseErrorHandler {

    /**
     * Determine if the response is an error.
     * @param clientHttpResponse    The response from the REST call.
     * @return                      A boolean value indicating status of the response.
     * @throws IOException          In case of error while reading the status.
     */
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return !clientHttpResponse.getStatusCode().is2xxSuccessful();
    }

    /**
     * Given the behaviour of the CRM POX services we will never encounter a clinet series errors
     * It will always be 200 series or 500 series.
     * @param clientHttpResponse    The response from the REST call.
     * @throws IOException          In case of error while reading the message or handling the error.
     */
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        // read the message and determine the error.
        Error error = readXMLMessage(clientHttpResponse.getBody());

        if (error != null) {
            log.error("Error while invoking the CRM customer profile endpoint : error code {}", error.getCode());

            if (Constants.CUSTOMER_EXISTS.equals(error.getCode())) {
                throw new CustomerAlreadyExistsException();
            } else if (Constants.CUSTOMER_CREATION_FAILED.equals(error.getCode())) {
                throw new CustomerCreationFailedException();
            } else if (Constants.CUSTOMER_CAN_NOT_BE_FOUND.equals(error.getCode())) {
                throw new CustomerCanNotBeFoundException();
            } else if (Constants.CUSTOMER_UPDATE_FAILED.equals(error.getCode())) {
                throw new CustomerUpdateFailedException();
            } else if (Constants.CUSTOMER_DELETE_FAILED.equals(error.getCode())) {
                throw new CustomerDeleteFailed();
            } else {
                throw new CustomerServiceException("Unknown error occurred while invoking the CRM endpoint.");
            }
        } else {
            throw new CustomerServiceException("Unknown error occurred while invoking the CRM endpoint.");
        }
    }

    /**
     * Read the XML message from the wire, this process is bit slow due to the JAXB unmarshaller.
     * @param inputStream       Input steam of the message body.
     * @return                  Build result object.
     * @throws JAXBException    In case of error while reading the message.
     */
    private Error readXMLMessage(InputStream inputStream) {

        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Error.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Error) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            log.error("Error while reading the message", e);
        }
        return null;
    }
}
