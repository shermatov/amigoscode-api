package com.shermatov.journey;

import com.shermatov.customer.Customer;
import com.shermatov.customer.CustomerRegistrationRequest;
import com.shermatov.customer.CustomerUpdateRequest;
import com.shermatov.customer.Gender;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {
    protected Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";


    @Autowired
    private WebTestClient webClient;

    @Test
    void canRegisterACustomer() {
        // Create a registration request
        Faker FAKER = new Faker();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        String email = firstName + "_" + lastName + "@shermatovTest.com";
        int age = RANDOM.nextInt(1, 101);
        String name = firstName + " " + lastName;

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender);

        // Send a post-request

        webClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // Get all customers
        List<Customer> allCustomers = webClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Customer>>() {
                })
                .returnResult()
                .getResponseBody();

// Verify newly added customer is present
        Customer expectedCustomer = new Customer(name, email, age, gender);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

// Get correct ID from List
        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        expectedCustomer.setId(id);

// Get customer by ID and verify
        webClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .isEqualTo(expectedCustomer);


        // get customer by id

    }

    @Test
    void canDeleteACustomer() {
        // Create a registration request
        Faker FAKER = new Faker();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        String email = firstName + "_" + lastName + "@shermatovTest.com";
        int age = RANDOM.nextInt(1, 101);
        String name = firstName + " " + lastName;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender);

        // Send a post-request

        webClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // Get all customers
        List<Customer> allCustomers = webClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Customer>>() {
                })
                .returnResult()
                .getResponseBody();

// Verify newly added customer is present

// Get the correct ID from a list
        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete the customer
        webClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

// Get customer by ID and verify
        webClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();


        // get customer by id

    }

    @Test
    void canUpdateACustomer() {
        // Create a registration request
        Faker FAKER = new Faker();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        String email = firstName + "_" + lastName + "@shermatovTest.com";
        int age = RANDOM.nextInt(1, 101);
        String name = firstName + " " + lastName;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender);

        // Send a post-request

        webClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // Get all customers
        List<Customer> allCustomers = webClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Customer>>() {
                })
                .returnResult()
                .getResponseBody();

// Verify newly added customer is present

// Get correct ID from a list
        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update the customer
        String newName = "Ali";

        CustomerUpdateRequest requestUpdate = new CustomerUpdateRequest(
                newName, null, null
        );
        webClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestUpdate), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

// Get customer by ID and verify
        Customer updatedCustomer = webClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(
                id, name, email, age, gender
        );

        assertThat(updatedCustomer).isEqualTo(expected);



        // get customer by id

    }
}
