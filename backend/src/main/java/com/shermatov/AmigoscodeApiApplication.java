package com.shermatov;

import com.shermatov.customer.Customer;
import com.shermatov.customer.CustomerRepository;
import com.shermatov.customer.Gender;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class AmigoscodeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmigoscodeApiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            Random random = new Random();
            String lastName = faker.name().lastName();
            String firstName = faker.name().firstName();
            int age = random.nextInt(100) + 2;
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


            Customer customer = new Customer(
                    firstName + " " + lastName,
                    firstName + "_" + lastName + "@shermatov.com",
                    age,
                    gender);

            customerRepository.save(customer);
        };
    }


}
