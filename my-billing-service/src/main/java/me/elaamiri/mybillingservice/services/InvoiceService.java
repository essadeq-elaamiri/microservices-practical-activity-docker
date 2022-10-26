package me.elaamiri.mybillingservice.services;

import me.elaamiri.mybillingservice.dtos.InvoiceRequestDTO;
import me.elaamiri.mybillingservice.dtos.InvoiceResponseDTO;
import me.elaamiri.mybillingservice.entities.Invoice;

import java.util.List;

public interface InvoiceService {

    // list all
    List<InvoiceResponseDTO> getInvoicesList(int page, int size);
    // get one
    InvoiceResponseDTO getInvoiceById(String id);
    // insert
    InvoiceResponseDTO saveInvoice(InvoiceRequestDTO invoiceRequestDTO);
    // update
    InvoiceResponseDTO updateInvoice(String id, InvoiceRequestDTO invoiceRequestDTO);
    // delete
    boolean deleteInvoice(String id);
    // get customer's invoices
    List<InvoiceResponseDTO> getInvoicesListByCustomer(String customerId);

}
