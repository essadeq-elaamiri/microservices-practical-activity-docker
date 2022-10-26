package me.elaamiri.customerservice.controllers;

import lombok.AllArgsConstructor;
import me.elaamiri.customerservice.dtos.CustomerRequestDTO;
import me.elaamiri.customerservice.dtos.CustomerResponseDTO;
import me.elaamiri.customerservice.services.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api-v1/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @GetMapping("/")
    public List<CustomerResponseDTO> getCustomerList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size ){

        return customerService.getAllCustomers(page, size);
    }

    @GetMapping ("/{customerId}")
    public CustomerResponseDTO getCustomer(@PathVariable(name = "customerId") String id){
        return customerService.getCustomer(id);
    }

    @PostMapping("/")
    public CustomerResponseDTO insertCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        return customerService.saveCustomer(customerRequestDTO);
    }

    @PutMapping("/{customerId}")
    public CustomerResponseDTO updateCustomer(@PathVariable(name = "customerId") String id,
                                              @RequestBody CustomerRequestDTO customerRequestDTO){
        return customerService.updateCustomer(id, customerRequestDTO);
    }

    @DeleteMapping("/{customerId}")
    public boolean updateCustomer(@PathVariable(name = "customerId") String id){
        return customerService.deleteCustomer(id);
    }

}
