package me.elaamiri.mybillingservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.elaamiri.mybillingservice.entities.helperModels.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

// facture
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Invoice {
    @Id
    private String id;
    @Temporal(TemporalType.DATE)
    private Date date;
    private BigDecimal amount;
    @NotBlank
    private String customerID;
    @Transient
    private Customer customer;
}
