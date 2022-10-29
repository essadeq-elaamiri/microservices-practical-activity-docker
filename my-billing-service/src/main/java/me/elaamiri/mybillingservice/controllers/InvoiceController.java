package me.elaamiri.mybillingservice.controllers;

import lombok.AllArgsConstructor;
import me.elaamiri.mybillingservice.dtos.InvoiceRequestDTO;
import me.elaamiri.mybillingservice.dtos.InvoiceResponseDTO;
import me.elaamiri.mybillingservice.services.InvoiceService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api-v1/invoices")
@AllArgsConstructor
public class InvoiceController {
    InvoiceService invoiceService;

    // list pagination
    @GetMapping("/")
    public List<InvoiceResponseDTO> getInvoicesList(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size){
        return invoiceService.getInvoicesList(page, size);
    }
    // list customer invoices
    @GetMapping("/customer/{customerId}")
    public List<InvoiceResponseDTO> getCustomerInvoices(@PathVariable String customerId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size){
        return invoiceService.getInvoicesListByCustomer(customerId, page, size);
    }

    // get one by id
    @GetMapping("/{invoiceId}")
    public InvoiceResponseDTO getInvoice(@PathVariable String invoiceId){
        return invoiceService.getInvoiceById(invoiceId);
    }
    // insert

    @PostMapping("/")
    public InvoiceResponseDTO insertInvoice(@RequestBody @Valid InvoiceRequestDTO invoiceRequestDTO){
        return invoiceService.saveInvoice(invoiceRequestDTO);
    }

    // update
    @PutMapping("/{invoiceId}")
    public InvoiceResponseDTO updateInvoice(@PathVariable String invoiceId,@RequestBody @Valid InvoiceRequestDTO invoiceRequestDTO){
        return invoiceService.updateInvoice(invoiceId, invoiceRequestDTO);
    }
    // delete
    @DeleteMapping("/{invoiceId}")
    public boolean deleteInvoice(@PathVariable String invoiceId){
        return invoiceService.deleteInvoice(invoiceId);
    }

}
