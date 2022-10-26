package me.elaamiri.mybillingservice.openFeign;

import me.elaamiri.mybillingservice.entities.helperModels.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "CUSTOMER-SERVICE")
// declare a rest client connected to CUSTOMER-SERVICE
public interface CustomerServiceRestClient {
    @GetMapping(path = "/api-v1/customers/{id}")
    Customer getCustomerById(@PathVariable String id);
    @GetMapping(path = "/api-v1/customers/")
    List<Customer> getCustomers(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size);
}
