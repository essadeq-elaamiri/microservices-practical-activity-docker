package me.elaamiri.mybillingservice.services;

import lombok.AllArgsConstructor;
import me.elaamiri.mybillingservice.dtos.InvoiceRequestDTO;
import me.elaamiri.mybillingservice.dtos.InvoiceResponseDTO;
import me.elaamiri.mybillingservice.entities.Invoice;
import me.elaamiri.mybillingservice.mappers.InvoiceMapper;
import me.elaamiri.mybillingservice.repositories.InvoiceRepository;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceServiceImp implements InvoiceService{

    InvoiceRepository invoiceRepository;
    InvoiceMapper invoiceMapper;

    @Override
    public List<InvoiceResponseDTO> getInvoicesList(int page, int size) {
        List<Invoice> invoiceList = invoiceRepository.findAll(PageRequest.of(page, size)).getContent();
        List<InvoiceResponseDTO> invoiceResponseDTOList = invoiceList.stream().map(
                invoice -> {
                 return invoiceMapper.toInvoiceResponse(invoice);
                }
        ).collect(Collectors.toList());
        return invoiceResponseDTOList;
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(String id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Invoice with ID: %s Not Found !", id)));

        return invoiceMapper.toInvoiceResponse(invoice);
    }

    @Override
    public InvoiceResponseDTO saveInvoice(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = invoiceMapper.toInvoice(invoiceRequestDTO);
        return invoiceMapper.toInvoiceResponse(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponseDTO updateInvoice(String id, InvoiceRequestDTO invoiceRequestDTO) {
        invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Invoice with ID: %s Not Found !", id)));
        Invoice invoiceToSave = invoiceMapper.toInvoice(invoiceRequestDTO);
        invoiceToSave.setId(id);
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
        List<InvoiceResponseDTO> invoiceResponseDTOList = invoiceList.stream().map(
                invoice -> {
                    return invoiceMapper.toInvoiceResponse(invoice);
                }
        ).collect(Collectors.toList());
        return invoiceResponseDTOList;
    }
}
