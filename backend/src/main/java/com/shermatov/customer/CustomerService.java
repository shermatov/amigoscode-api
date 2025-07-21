package com.shermatov.customer;

import com.shermatov.exception.DuplicateResourceException;
import com.shermatov.exception.RequestValidationException;
import com.shermatov.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;


    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
    public List<Customer> getAllCustomers () {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer customerID) {
        return customerDAO.selectCustomerById(customerID)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer with [%s] not found".formatted(customerID)
                        )
                );

    }


    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if(customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException(
                    "Customer with [%s] is already registered".formatted(customerRegistrationRequest.email())
            );
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age());
        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomerWithId(Integer customerID) {
        if(!customerDAO.existsCustomerWithID(customerID)) {
            throw new ResourceNotFoundException("Customer with [%s] not found".formatted(customerID));
        }
        customerDAO.deleteCustomerById(customerID);


    }

    public void updateCustomer(Integer customerID, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(customerID);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {

            if (customerDAO.existsCustomerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "Customer with [%s] email  is already registered".formatted(customer.getEmail())
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if(!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDAO.updateCustomer(customer);


    }
}
