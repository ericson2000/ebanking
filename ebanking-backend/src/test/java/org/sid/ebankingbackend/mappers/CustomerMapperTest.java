package org.sid.ebankingbackend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;

public class CustomerMapperTest {

    @Test
    void given_customer_id_map_customerDto_id(){
        //GIVEN
        Customer customer = Customer.builder().id(1l).build();

        //WHEN
        final CustomerDto customerDto = CustomerMapper.INSTANCE.customerToCustomerDto(customer);

        //THEN
        Assertions.assertEquals(customer.getId(),customerDto.getId());
    }

    @Test
    void given_customer_name_map_customerDto_name(){
        //GIVEN
        Customer customer = Customer.builder().name("name").build();

        //WHEN
        final CustomerDto customerDto = CustomerMapper.INSTANCE.customerToCustomerDto(customer);

        //THEN
        Assertions.assertEquals(customer.getName(),customerDto.getName());
    }

    @Test
    void given_customer_email_map_customerDto_email(){
        //GIVEN
        Customer customer = Customer.builder().email("email").build();

        //WHEN
        final CustomerDto customerDto = CustomerMapper.INSTANCE.customerToCustomerDto(customer);

        //THEN
        Assertions.assertEquals(customer.getEmail(),customerDto.getEmail());
    }

    @Test
    void given_customerDto_id_map_customer_id(){
        //GIVEN
        CustomerDto customerDto = CustomerDto.builder().id(1L).build();

        //WHEN
        final Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerDto);

        //THEN
        Assertions.assertEquals(customerDto.getId(),customer.getId());
    }

    @Test
    void given_customerDto_email_map_customer_email(){
        //GIVEN
        CustomerDto customerDto = CustomerDto.builder().email("email").build();

        //WHEN
        final Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerDto);

        //THEN
        Assertions.assertEquals(customerDto.getEmail(),customer.getEmail());
    }

    @Test
    void given_customerDto_name_map_customer_email(){
        //GIVEN
        CustomerDto customerDto = CustomerDto.builder().name("email").build();

        //WHEN
        final Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerDto);

        //THEN
        Assertions.assertEquals(customerDto.getName(),customer.getName());
    }
}
