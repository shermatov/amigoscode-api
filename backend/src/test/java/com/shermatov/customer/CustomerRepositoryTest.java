package com.shermatov.customer;

import com.shermatov.AbstractTestcontainersUnitTest;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Java6Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainersUnitTest {

    private final Faker FAKER = new Faker();
    String firstName = FAKER.name().firstName();
    String lastName = FAKER.name().lastName();
    private Customer customer;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                firstName + " " + lastName,
                firstName + "_" + lastName + "@shermatov.com",
                RANDOM.nextInt(100) + 2
        );
        System.out.println(applicationContext.getBeanDefinitionCount());
    }


    @Autowired
     private CustomerRepository underTest;


    @Test
    void existsCustomerByEmail() {
        // Given
        String email = firstName + "_" + lastName + "@shermatov.com";

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        // When
        var actual = underTest.existsCustomerByEmail(email);


        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerById() {
        String email = firstName + "_" + lastName + "@shermatov.com";

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        // When
        var actual = underTest.existsCustomerById(id);


        // Then
        assertThat(actual).isTrue();
    }
}