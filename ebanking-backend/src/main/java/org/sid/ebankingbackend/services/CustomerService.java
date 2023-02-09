package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

    CustomerDto saveCustomer(CustomerDto customerDto);

    List<CustomerDto> listCustomer();

    CustomerDto getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDto updateCustomer(CustomerDto customerDto);

    void deleteCustomer(Long customerId);
}
