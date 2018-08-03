package com.techtest.customer.controllers;

import com.techtest.customer.exp.*;
import com.techtest.customer.model.Customer;
import com.techtest.customer.model.ErrorResponse;
import com.techtest.customer.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *  This controller class act as the entry point of the customer profile API.
 *  It provides GET, PUT, POST, DELETE operations on top of the customer profile
 *  and it keeps all the HTTP transport level details with it hiding those from the
 *  business logic.
 *
 */
@Slf4j
@RestController
public class CustomerServiceController extends BaseController {

    /**
     *  Reference to the customerService which holds the business logic.
     */
    private CustomerService customerService;

    CustomerServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Provides the functionality to create a customer profile record with given profile information.
     * @param customer  The customer profile information.
     * @return          Response entity with HTTP status code indicating status of the operation.
     * @throws CustomerAlreadyExistsException   If customer already exists
     * @throws CustomerCreationFailedException  If customer record creation failed due to system error.
     */
    @PostMapping("/customers")
    public ResponseEntity createCustomer(@RequestBody Customer customer)
            throws CustomerAlreadyExistsException, CustomerCreationFailedException {
        log.info(" > POST /customers");
        customerService.createCustomer(customer);
        log.info(" < POST /customers");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Provides the functionality to fetch a customer profile by given profile id.
     * @param id    Customer profile id.
     * @return      A customer profile record.
     * @throws CustomerCanNotBeFoundException If customer profile can not be found for the given profile id.
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id)
            throws CustomerCanNotBeFoundException, InvalidProfileIdException, CustomerReadFailedException {

        if (StringUtils.isEmpty(id) || StringUtils.isBlank(id)) {
            throw new InvalidProfileIdException();
        }

        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Provides the functionality to update customer profile record with given profile information.
     * @param customer  The customer profile information.
     * @return          Response entity with HTTP status code indicating status of the operation.
     * @throws CustomerCanNotBeFoundException   If customer profile can not be found for the given profile id.
     */
    @PutMapping("/customers")
    public ResponseEntity updateCustomer(@RequestBody Customer customer)
            throws CustomerCanNotBeFoundException, CustomerUpdateFailedException {

        customerService.updateCustomer(customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Provides the functionality to delete customer profile record with given profile id.
     * @param id    Customer profile id.
     * @return      Response entity with HTTP status code indicating status of the operation.
     * @throws CustomerCanNotBeFoundException    If customer profile can not be found for the given profile id.
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity deleteCustomer(@PathVariable String id)
            throws CustomerCanNotBeFoundException, CustomerDeleteFailedException {

        customerService.deleteCustomerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Convert CustomerAlreadyExistsException to HTTPStatus 409 Conflict.
     * @param ex    CustomerAlreadyExistsException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerRecordExists",
                "Customer profile exists with the same details.");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Convert CustomerCreationFailedException to HTTPStatus 500 Internal Server Error.
     * @param ex    CustomerCreationFailedException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(CustomerCreationFailedException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCreationFailedException(CustomerCreationFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerRecordCreationFailed",
                "Customer profile creation failed due to system error.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Convert InvalidProfileIdException to HTTPStatus 400 Bad Request.
     * @param ex    InvalidProfileIdException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(InvalidProfileIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProfileIdException(InvalidProfileIdException ex) {
        ErrorResponse errorResponse = new ErrorResponse("InvalidProfileIdProvided",
                "Customer profile id can not be empty or null");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Convert CustomerCanNotBeFoundException to HTTPStatus 404 Not Found.
     * @param ex    CustomerCanNotBeFoundException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(CustomerCanNotBeFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCanNotBeFoundException(CustomerCanNotBeFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerCanNotBeFound",
                "Customer profile could not be found for the given profile id.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Convert CustomerUpdateFailedException to HTTPStatus 500 Internal Server Error.
     * @param ex    CustomerUpdateFailedException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(CustomerUpdateFailedException.class)
    public ResponseEntity<ErrorResponse> handleCustomerUpdateFailedException(CustomerUpdateFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerUpdateFailed",
                "Customer profile update failed due to system error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Convert CustomerDeleteFailedException to HTTPStatus 500 Internal Server Error.
     * @param ex    CustomerDeleteFailedException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(CustomerDeleteFailedException.class)
    public ResponseEntity<ErrorResponse> handleCustomerDeleteFailed(CustomerDeleteFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerDeleteFailedException",
                "Customer profile delete failed due to system error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Convert CustomerReadFailedException to HTTPStatus 500 Internal Server Error.
     * @param ex    CustomerReadFailedException
     * @return      ResponseEntity with detailed error errorMessage.
     */
    @ExceptionHandler(CustomerReadFailedException.class)
    public ResponseEntity<ErrorResponse> handleCustomerReadFailedException(CustomerReadFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerReadFailedException",
                "Customer profile read failed due to system error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Convert CustomerServiceException to HTTPStatus 500 Internal Server Error.
     * @param ex    CustomerServiceException
     * @return      ResponseEntity with error errorMessage.
     */
    @ExceptionHandler(CustomerServiceException.class)
    public ResponseEntity<ErrorResponse> handleCustomerServiceException(CustomerServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse("SystemError",
                "Unknown system error.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
