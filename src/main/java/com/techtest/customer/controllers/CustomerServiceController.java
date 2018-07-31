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
public class CustomerServiceController {

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

        customerService.createCustomer(customer);
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
            throws CustomerCanNotBeFoundException, InvalidProfileIdException {

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
            throws CustomerCanNotBeFoundException, CustomerDeleteFailed {

        customerService.deleteCustomerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerRecordExists",
                "Customer profile exists with the same details.");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerCreationFailedException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCreationFailedException(CustomerCreationFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerRecordCreationFailed",
                "Customer profile creation failed due to system error.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProfileIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProfileIdException(InvalidProfileIdException ex) {
        ErrorResponse errorResponse = new ErrorResponse("InvalidProfileIdProvided",
                "Customer profile id can not be empty or null");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerCanNotBeFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCanNotBeFoundException(CustomerCanNotBeFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerCanNotBeFound",
                "Customer profile could not be found for the given profile id.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerUpdateFailedException.class)
    public ResponseEntity<ErrorResponse> handleCustomerUpdateFailedException(CustomerUpdateFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerUpdateFailed",
                "Customer profile update failed due to system error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomerDeleteFailed.class)
    public ResponseEntity<ErrorResponse> handleCustomerDeleteFailed(CustomerDeleteFailed ex) {
        ErrorResponse errorResponse = new ErrorResponse("CustomerDeleteFailed",
                "Customer profile delete failed due to system error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomerServiceException.class)
    public ResponseEntity<ErrorResponse> handleCustomerServiceException(CustomerServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse("SystemError",
                "Unknown system error.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
