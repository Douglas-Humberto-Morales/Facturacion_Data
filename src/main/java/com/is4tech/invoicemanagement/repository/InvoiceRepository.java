package com.is4tech.invoicemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.is4tech.invoicemanagement.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer>{
}
