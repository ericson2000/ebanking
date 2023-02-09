package org.sid.ebankingbackend.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CustomerMapper;
import org.sid.ebankingbackend.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class CustomerRestController {

    private CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/customers")
    public List<CustomerDto> customers() {

        return customerService.listCustomer();

    }

    @GetMapping("/customers/{id}")
    public CustomerDto getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {

        return customerService.getCustomer(customerId);
    }

    @PostMapping(value = "/customers",consumes = "application/json",produces = "application/json")
    public CustomerDto saveCustomer(@RequestBody CustomerDto customerDto) {

        return customerService.saveCustomer(customerDto);

    }

    @PutMapping(value = "/customers/{id}",consumes = "application/json",produces = "application/json")
    public CustomerDto saveCustomer(@PathVariable(name = "id") Long customerId, @RequestBody CustomerDto customerDto) {

        customerDto.setId(customerId);
        return customerService.updateCustomer(customerDto);

    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId)  {

        customerService.deleteCustomer(customerId);
    }


}
