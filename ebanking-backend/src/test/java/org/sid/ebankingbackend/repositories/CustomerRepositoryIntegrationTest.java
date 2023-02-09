package org.sid.ebankingbackend.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class})
@DirtiesContext
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void setupDatabase(){
        this.customerRepository.deleteAll();
    }

    @Test
    void given_customer_when_save_then_customer_persisted(){
        //GIVEN
        Customer customer = CustomerPopulator.createCustomer("name", "email@google.com");

        //WHEN
        final Customer persistedCustomer = customerRepository.save(customer);
        Optional<Customer> optionalCustomer = customerRepository.findById(persistedCustomer.getId());

        //THEN
        Assertions.assertEquals(customer,persistedCustomer);
        Assertions.assertTrue(optionalCustomer.isPresent());

    }

    @Test
    void given_customer_persisted_when_findById_then_return_customer(){

        //GIVEN
        Customer customer = CustomerPopulator.createCustomer("name", "email@google.com");
        final Customer persistedCustomer = customerRepository.save(customer);

        //WHEN
        final Optional<Customer> retrievedCustomer = customerRepository.findById(persistedCustomer.getId());

        //THEN
        Assertions.assertEquals(customer.getName(),retrievedCustomer.get().getName());
        Assertions.assertEquals(customer.getEmail(),retrievedCustomer.get().getEmail());
    }

    @Test
    void given_customer_persisted_when_update_then_return_customer_updated(){

        //GIVEN
        Customer customer = CustomerPopulator.createCustomer("name", "email@google.com");
        Customer persistedCustomer = customerRepository.save(customer);
        persistedCustomer.setEmail("updatedEmail");

        //WHEN
        final Customer updatedCustomer = customerRepository.save(persistedCustomer);

        //THEN
        Assertions.assertEquals(updatedCustomer.getEmail(),persistedCustomer.getEmail());
    }

    @Test
    void given_customer_created_when_delete_then_customer_is_not_present(){
        //GIVEN
        Customer customer = CustomerPopulator.createCustomer("name", "email@google.com");

        //WHEN
        final Customer persistedCustomer = customerRepository.save(customer);
        customerRepository.delete(persistedCustomer);
        final Optional<Customer> retrievedCustomer = customerRepository.findById(persistedCustomer.getId());

        //THEN
        Assertions.assertTrue(retrievedCustomer.isEmpty());

    }
}
