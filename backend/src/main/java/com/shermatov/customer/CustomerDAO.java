package com.shermatov.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerID);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerWithID(Integer customerId);
    void deleteCustomerById(Integer customerID);
    void updateCustomer(Customer update);

}
