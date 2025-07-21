package com.shermatov.customer;

import com.shermatov.exception.DuplicateResourceException;
import com.shermatov.exception.RequestValidationException;
import com.shermatov.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        verify(customerDAO).selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "Ali@gmail.com",
                15
        );
        Mockito.when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(customerId);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        int customerId = 10;

        Mockito.when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.empty());

        // When

        // Then
        assertThatThrownBy(() -> underTest.getCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with [%s] not found".formatted(customerId));
    }

    @Test
    void addCustomer() {
        // Given
        String email = "ali@gmail.com";

        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "ali",
                email,
                12
        );
        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }
    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "ali@gmail.com";

        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "ali",
                email,
                12
        );
        // When

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with [%s] is already registered".formatted(request.email())
                );

        // Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerWithId() {
        // Given
        int customerId = 1;

        when(customerDAO.existsCustomerWithID(customerId)).thenReturn(true);
        underTest.deleteCustomerWithId(customerId);

        // When
        verify(customerDAO).deleteCustomerById(customerId);

        // Then
    }

    @Test
    void willThrowDeleteCustomerByIdNotExists() {
        // Given
        int customerId = 1;

        when(customerDAO.existsCustomerWithID(customerId)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerWithId(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with [%s] not found".formatted(customerId));


        verify(customerDAO, never()).deleteCustomerById(any());

        // Then
    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "ali@gmail.com",
                15
        );
        String email = "aibek@gmail.com";


        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("ali", email, 16);
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(false);

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "ali@gmail.com",
                15
        );


        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Aibek", null, null);

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }
    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "ali@gmail.com",
                15
        );


        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 100);

        underTest.updateCustomer(customerId, updateRequest);


        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }
    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "ali@gmail.com",
                15
        );


        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, "kachok", null);

        when(customerDAO.existsCustomerWithEmail(anyString())).thenReturn(false);

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void willThrowExceptionWhenSameEmailRegisteredBeforeUpdate() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "ali@gmail.com",
                15
        );


        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, "kachok", null);

        when(customerDAO.existsCustomerWithEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(customerId, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with [%s] email  is already registered".formatted(customer.getEmail()));

        // Then
        verify(customerDAO, never()).updateCustomer(any());

    }

    @Test
    void willThrowExceptionWhenNoChangesHappen() {
        // Given
        int customerId = 1;
        Customer customer = new Customer(
                customerId,
                "ali",
                "ali@gmail.com",
                15
        );


        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // When

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());


        assertThatThrownBy(() -> underTest.updateCustomer(customerId, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage(("no data changes found"));
        // Then
        verify(customerDAO, never()).updateCustomer(any());

    }


}