package com.shermatov.customer;

import com.shermatov.AbstractTestcontainersUnitTest;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDAOServiceTest extends AbstractTestcontainersUnitTest {

    private CustomerJDBCDAOService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private final Faker FAKER = new Faker();
    String firstName = FAKER.name().firstName();
    String lastName = FAKER.name().lastName();
    private Customer customer;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDAOService(
                getJdbcTemplate(),
                customerRowMapper
        );



        customer = new Customer(
                firstName + " " + lastName,
                firstName + "_" + lastName + "@shermatov.com",
                RANDOM.nextInt(100) + 2
        );
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void selectAllCustomers() {
        // Given
        underTest.insertCustomer(customer);

        // When
        List<Customer> customers = underTest.selectAllCustomers();

        // Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> customer = underTest.selectCustomerById(id);

        // Then
        assertThat(customer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customer.get().getId());
            assertThat(c.getEmail()).isEqualTo(customer.get().getEmail());
            assertThat(c.getName()).isEqualTo(customer.get().getName());
            assertThat(c.getAge()).isEqualTo(customer.get().getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given

        int id = 0;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void existsCustomerWithEmail() {
        // Given

        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existsCustomerWithEmail(email);

        // Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
        boolean exists = underTest.existsCustomerWithEmail("");
        assertThat(exists).isFalse();;
    }

    @Test
    void existsCustomerWithID() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsCustomerWithID(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existsCustomerWithID(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);

        // Then

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newEmail = "foo@shermatov.com";

        // When

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);
        // Then

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }

    @Test
    void updateCustomerAge() {
        // Given

        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        int newAge = 30;

        // When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void wilNotUpdateWhenNothingToUpdate() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(id);
        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void wilUpdateAllPropertiesCustomer() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        String newEmail = FAKER.name().firstName() + FAKER.name().lastName() + "@shermatov.com";
        update.setEmail(newEmail);
        update.setAge(30);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo("foo");
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(30);
        });
        
        
    }
}