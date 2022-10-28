package me.elaamiri.mybillingservice;

import me.elaamiri.mybillingservice.dtos.InvoiceRequestDTO;
import me.elaamiri.mybillingservice.dtos.InvoiceResponseDTO;
import me.elaamiri.mybillingservice.entities.Invoice;
import me.elaamiri.mybillingservice.entities.helperModels.Customer;
import me.elaamiri.mybillingservice.services.InvoiceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;


// Activating openFeign
@EnableFeignClients
@SpringBootApplication
public class MyBillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(InvoiceService invoiceService){
        return args -> {
            String customerId = "96d2de30-a0dd-4243-8806-b6748e49e763";
            InvoiceRequestDTO invoiceRequestDTO = new InvoiceRequestDTO();
            invoiceRequestDTO.setAmount(BigDecimal.valueOf(17885.2));
            invoiceRequestDTO.setCustomerId(customerId);
            InvoiceResponseDTO invoice = invoiceService.saveInvoice(invoiceRequestDTO);

        };
    }
}
