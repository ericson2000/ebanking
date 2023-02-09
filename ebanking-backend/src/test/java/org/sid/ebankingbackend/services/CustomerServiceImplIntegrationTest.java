package org.sid.ebankingbackend.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class})
@DirtiesContext
public class CustomerServiceImplIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void deleteAll(){
        customerRepository.deleteAll();
    }

    @Test
    void given_customer_when_saveCustomer_then_return_customer(){
        //GIVEN
        CustomerDto customerDto = CustomerPopulator.createCustomerDto("eric","eric@gmail.com");

        //WHEN
        final  CustomerDto persistedCustomerDto = customerService.saveCustomer(customerDto);

        //THEN
        Assertions.assertEquals(persistedCustomerDto.getEmail(), customerDto.getEmail());
        Assertions.assertEquals(persistedCustomerDto.getName(), customerDto.getName());
    }

    @Test
    void given_2_customers_persisted_when_listCustomer_then_return_list_of_2customers(){
        //GIVEN
        CustomerDto firstCustomerDto = CustomerPopulator.createCustomerDto("eric","eric@gmail.com");
        CustomerDto secondCustomerDto = CustomerPopulator.createCustomerDto("eric","eric@gmail.com");
        customerService.saveCustomer(firstCustomerDto);
        customerService.saveCustomer(secondCustomerDto);

        //WHEN
        final List<CustomerDto> customerDtos = customerService.listCustomer();

        //THEN
        Assertions.assertEquals(2, customerDtos.size());
    }

    @Test
    void given_customers_persisted_when_getCustomer_then_return_customerDto() throws CustomerNotFoundException {
        //GIVEN
        CustomerDto customerDto = CustomerPopulator.createCustomerDto("eric","eric@gmail.com");
        CustomerDto persistedCustomer = customerService.saveCustomer(customerDto);

        //WHEN
        final CustomerDto retrievedCustomerDto = customerService.getCustomer(persistedCustomer.getId());

        //THEN
        Assertions.assertEquals(retrievedCustomerDto.getId(), persistedCustomer.getId());
        Assertions.assertEquals(retrievedCustomerDto.getName(), persistedCustomer.getName());
        Assertions.assertEquals(retrievedCustomerDto.getEmail(), persistedCustomer.getEmail());
    }

    @Test
    void given_customers_not_exist_when_getCustomer_then_return_throw_CustomerNotFoundException(){
        //GIVEN
        Customer customer = CustomerPopulator.createCustomer("eric","eric@gmail.com");

        //WHEN
        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class,() -> {
            customerService.getCustomer(200L);
        });

        //THEN
        Assertions.assertEquals("Customer Not Found",exception.getMessage());
    }

    @Test
    void given_customer_persisted_when_updateCustomer_then_return_customer() {
        //GIVEN
        CustomerDto customerDto = CustomerPopulator.createCustomerDto("eric","eric@gmail.com");
        CustomerDto persistedCustomer = customerService.saveCustomer(customerDto);
        persistedCustomer.setEmail("update@gmail.com");
        persistedCustomer.setName("updateName");

        //WHEN
        final CustomerDto updateCustomerDto = customerService.updateCustomer(persistedCustomer);

        //THEN
        Assertions.assertEquals(persistedCustomer.getName(), updateCustomerDto.getName());
        Assertions.assertEquals(persistedCustomer.getEmail(), updateCustomerDto.getEmail());
    }

    @Test
    void given_customer_persisted_when_deleteCustomer_then_customer_does_not_exists() {
        //GIVEN
        CustomerDto customerDto = CustomerPopulator.createCustomerDto("eric","eric@gmail.com");
        CustomerDto persistedCustomer = customerService.saveCustomer(customerDto);

        //WHEN
        customerService.deleteCustomer(persistedCustomer.getId());
        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class,() -> {
            customerService.getCustomer(persistedCustomer.getId());
        });

        //THEN
        Assertions.assertEquals("Customer Not Found",exception.getMessage());
    }
}
