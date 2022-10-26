package me.elaamiri.mybillingservice.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InvoiceResponseDTO {
    private String id;
    private Date date;
    private BigDecimal amount;
}
