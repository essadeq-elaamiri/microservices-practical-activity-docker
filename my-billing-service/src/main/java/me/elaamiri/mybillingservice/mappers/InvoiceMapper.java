package me.elaamiri.mybillingservice.mappers;

import me.elaamiri.mybillingservice.dtos.InvoiceRequestDTO;
import me.elaamiri.mybillingservice.dtos.InvoiceResponseDTO;
import me.elaamiri.mybillingservice.entities.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceResponseDTO toInvoiceResponse(Invoice invoice);
    Invoice toInvoice(InvoiceRequestDTO invoiceRequestDTO);
}
