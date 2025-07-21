package com.shermatov.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDAOService implements CustomerDAO {

    private final CustomerRepository customerRepository;

    public CustomerJpaDAOService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerID) {
        return customerRepository.findById(customerID);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerWithID(Integer customerId) {
        return customerRepository.existsCustomerById(customerId);
    }

    @Override
    public void deleteCustomerById(Integer customerID) {
        customerRepository.deleteById(customerID);
    }

    @Override
    public void updateCustomer(Customer update) {
        customerRepository.save(update);
    }
}
