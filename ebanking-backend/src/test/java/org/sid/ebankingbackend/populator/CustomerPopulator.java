package org.sid.ebankingbackend.populator;

import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;

public class CustomerPopulator {

    public static Customer createCustomer(String name, String email) {
        return Customer.builder().name(name).email(email).build();
    }

    public static CustomerDto createCustomerDto(String name, String email) {
        return CustomerDto.builder().name(name).email(email).build();
    }
}
