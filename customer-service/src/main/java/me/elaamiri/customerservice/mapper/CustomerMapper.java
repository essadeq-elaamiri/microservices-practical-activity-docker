package me.elaamiri.customerservice.mapper;

import me.elaamiri.customerservice.dtos.CustomerRequestDTO;
import me.elaamiri.customerservice.dtos.CustomerResponseDTO;
import me.elaamiri.customerservice.entities.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDTO toCustomerResponse(Customer customer);
    Customer toCustomer(CustomerRequestDTO customerRequestDTO);
}
