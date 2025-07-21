package com.shermatov.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    public CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerID}")
    public Customer getCustomer(@PathVariable("customerID") int customerID) {
        return customerService.getCustomer(customerID);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("/{customerID}")
    public void deleteCustomer(@PathVariable("customerID") int customerID) {
        customerService.deleteCustomerWithId(customerID);
    }

    @PutMapping("/{customerID}")
    public void updateCustomer(@PathVariable("customerID") int customerID,
                               @RequestBody CustomerUpdateRequest updateRequest) {
        customerService.updateCustomer(customerID, updateRequest);
    }
}
