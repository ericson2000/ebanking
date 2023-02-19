package org.sid.ebankingbackend.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CustomerMapper;
import org.sid.ebankingbackend.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CustomerDto>> customers() {

        return ResponseEntity.ok(customerService.listCustomer());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {

        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PostMapping(value = "/customers",consumes = "application/json",produces = "application/json")
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {

        return ResponseEntity.ok(customerService.saveCustomer(customerDto));
    }

    @PutMapping(value = "/customers/{id}",consumes = "application/json",produces = "application/json")
    public ResponseEntity<CustomerDto> saveCustomer(@PathVariable(name = "id") Long customerId, @RequestBody CustomerDto customerDto) {

        customerDto.setId(customerId);
        return ResponseEntity.ok(customerService.updateCustomer(customerDto));
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId)  {

        customerService.deleteCustomer(customerId);
    }


}
