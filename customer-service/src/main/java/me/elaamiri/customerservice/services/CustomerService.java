package me.elaamiri.customerservice.services;

import me.elaamiri.customerservice.dtos.CustomerRequestDTO;
import me.elaamiri.customerservice.dtos.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface CustomerService {

    CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDTO);

    CustomerResponseDTO getCustomer(String id);

    CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO customerRequestDTO);

    boolean deleteCustomer(String id);

    List<CustomerResponseDTO> getAllCustomers(int page, int size);
}
