package com.is4tech.invoicemanagement.repository;

import com.is4tech.invoicemanagement.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
