package me.elaamiri.mybillingservice.repositories;

import me.elaamiri.mybillingservice.entities.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Page<Invoice> findByCustomerID(String customerId, Pageable pageable);
}
