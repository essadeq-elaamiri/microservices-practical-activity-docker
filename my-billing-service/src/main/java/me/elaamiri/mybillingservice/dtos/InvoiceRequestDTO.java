package me.elaamiri.mybillingservice.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InvoiceRequestDTO {
    //private String id; // generated automatically , no need
    // private Date date; // generated automatically , no need
    private BigDecimal amount;
    private String customerId;

}
