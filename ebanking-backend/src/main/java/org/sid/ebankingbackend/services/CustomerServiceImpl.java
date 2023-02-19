package org.sid.ebankingbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CustomerMapper;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {

        log.info("Saving new Customer");

        Customer saveCustomer = customerRepository.save(CustomerMapper.INSTANCE.customerDtoToCustomer(customerDto));

        return CustomerMapper.INSTANCE.customerToCustomerDto(saveCustomer);
    }

    @Override
    public List<CustomerDto> listCustomer() {

        return customerRepository.findAll().stream().map(CustomerMapper.INSTANCE::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomer(Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));

        return CustomerMapper.INSTANCE.customerToCustomerDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {

        log.info("Saving new Customer");

        Customer saveCustomer = customerRepository.save(CustomerMapper.INSTANCE.customerDtoToCustomer(customerDto));

        return CustomerMapper.INSTANCE.customerToCustomerDto(saveCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){

        customerRepository.deleteById(customerId);
    }

}
