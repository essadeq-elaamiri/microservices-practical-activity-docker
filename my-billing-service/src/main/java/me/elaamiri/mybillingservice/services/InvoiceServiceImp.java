package me.elaamiri.mybillingservice.services;

import lombok.AllArgsConstructor;
import me.elaamiri.mybillingservice.dtos.InvoiceRequestDTO;
import me.elaamiri.mybillingservice.dtos.InvoiceResponseDTO;
import me.elaamiri.mybillingservice.entities.Invoice;
import me.elaamiri.mybillingservice.entities.helperModels.Customer;
import me.elaamiri.mybillingservice.mappers.InvoiceMapper;
import me.elaamiri.mybillingservice.openFeign.CustomerServiceRestClient;
import me.elaamiri.mybillingservice.repositories.InvoiceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class InvoiceServiceImp implements InvoiceService{

    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;

    private CustomerServiceRestClient customerServiceRestClient;

    @Override
    public List<InvoiceResponseDTO> getInvoicesList(int page, int size) {
        List<Invoice> invoiceList = invoiceRepository.findAll(PageRequest.of(page, size)).getContent();
        return getInvoiceResponseDTOS(invoiceList);
    }

    private List<InvoiceResponseDTO> getInvoiceResponseDTOS(List<Invoice> invoiceList) {
        List<InvoiceResponseDTO> invoiceResponseDTOList = invoiceList.stream().map(
                invoice -> {
                    Customer customer = customerServiceRestClient.getCustomerById(invoice.getCustomerID());
                    if (customer == null) throw new RuntimeException(String.format("Can Not Find Customer with ID: %s", invoice.getCustomerID()));
                    invoice.setCustomer(customer);
                    return invoiceMapper.toInvoiceResponse(invoice);
                }
        ).collect(Collectors.toList());
        return invoiceResponseDTOList;
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(String id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Invoice with ID: %s Not Found !", id)));
        Customer customer = customerServiceRestClient.getCustomerById(invoice.getCustomerID());
        if (customer == null) throw new RuntimeException(String.format("Can Not Find Customer with ID: %s", invoice.getCustomerID()));
        invoice.setCustomer(customer);
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    @Override
    public InvoiceResponseDTO saveInvoice(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = invoiceMapper.toInvoice(invoiceRequestDTO);
        invoice.setId(UUID.randomUUID().toString());
        invoice.setDate(new Date());
        // referential integrity check (validation)
        Customer customer = customerServiceRestClient.getCustomerById(invoiceRequestDTO.getCustomerID());
        if (customer == null) throw new RuntimeException(String.format("Can Not Find Customer with ID: %s", invoiceRequestDTO.getCustomerID()));
        invoice.setCustomerID(invoiceRequestDTO.getCustomerID());
        invoice.setCustomer(customer);
        InvoiceResponseDTO invoiceResponseDTO = invoiceMapper.toInvoiceResponse(invoiceRepository.save(invoice));
        invoiceResponseDTO.setCustomer(customer);
        return invoiceResponseDTO;
    }

    @Override
    public InvoiceResponseDTO updateInvoice(String id, InvoiceRequestDTO invoiceRequestDTO) {
        invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Invoice with ID: %s Not Found !", id)));
        Invoice invoiceToSave = invoiceMapper.toInvoice(invoiceRequestDTO);
        invoiceToSave.setId(id);
        Customer customer = customerServiceRestClient.getCustomerById(invoiceRequestDTO.getCustomerID());
        if (customer == null) throw new RuntimeException(String.format("Can Not Find Customer with ID: %s", invoiceRequestDTO.getCustomerID()));
        invoiceToSave.setCustomer(customer);
        return invoiceMapper.toInvoiceResponse(invoiceRepository.save(invoiceToSave));
    }

    @Override
    public boolean deleteInvoice(String id) {
        invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Invoice with ID: %s Not Found !", id)));
        invoiceRepository.deleteById(id);
        return true;
    }

    @Override
    public List<InvoiceResponseDTO> getInvoicesListByCustomer(String customerId, int page, int size) {
        List<Invoice> invoiceList = invoiceRepository.findByCustomerID(customerId, PageRequest.of(page, size)).getContent();
        return getInvoiceResponseDTOS(invoiceList);
    }
}
