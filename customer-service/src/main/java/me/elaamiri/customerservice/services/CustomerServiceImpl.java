package me.elaamiri.customerservice.services;

import lombok.AllArgsConstructor;
import me.elaamiri.customerservice.exceptions.CustomerNotFountException;
import me.elaamiri.customerservice.dtos.CustomerRequestDTO;
import me.elaamiri.customerservice.dtos.CustomerResponseDTO;
import me.elaamiri.customerservice.entities.Customer;
import me.elaamiri.customerservice.mapper.CustomerMapper;
import me.elaamiri.customerservice.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDTO){
        Customer customer = customerMapper.toCustomer(customerRequestDTO);
        customer.setId(UUID.randomUUID().toString());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(savedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomer(String id){
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow( () -> {
                    return  new CustomerNotFountException(String.format("Customer with ID: %s Not found !", id));
                });
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO customerRequestDTO){
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow( () -> {
                    return  new  CustomerNotFountException(String.format("Customer with ID: %s Not found !", id));
                });

        Customer customerMap = customerMapper.toCustomer(customerRequestDTO);
        customerMap.setId(id);
        Customer savedCustomer = customerRepository.save(customerMap);
        return customerMapper.toCustomerResponse(savedCustomer);
    }

    @Override
    public boolean deleteCustomer(String id){
        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers(int page, int size){
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));
        List<CustomerResponseDTO> customerResponseDTOList =
                customerPage.getContent().stream().map(customer -> customerMapper.toCustomerResponse(customer)).collect(Collectors.toList());
        return customerResponseDTOList;
    }

}
