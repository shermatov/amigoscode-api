package com.shermatov.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDAOService implements CustomerDAO {
    public static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                19

        );
        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                23
        );

        customers.add(alex);
        customers.add(jamila);

    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerID) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(customerID))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream().anyMatch(
                customer -> customer.getEmail().equals(email)
        );
    }

    @Override
    public boolean existsCustomerWithID(Integer customerId) {
        return customers.stream().anyMatch(
                customer -> customer.getId().equals(customerId)
        );
    }

    @Override
    public void deleteCustomerById(Integer customerID) {
        customers.removeIf(customer -> customer.getId().equals(customerID));
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }
}
