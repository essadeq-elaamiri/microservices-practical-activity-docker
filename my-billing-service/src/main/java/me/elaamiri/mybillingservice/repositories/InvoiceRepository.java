package me.elaamiri.mybillingservice.repositories;

import me.elaamiri.mybillingservice.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
}
